package com.example.uangku.feature.settings.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import com.example.uangku.core.ui.theme.redBg
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.feature.backup.presentation.BackupEffect
import com.example.uangku.feature.backup.presentation.BackupEvent
import com.example.uangku.feature.backup.presentation.BackupViewModel
import com.example.uangku.feature.backup.presentation.component.AutoBackupSection
import com.example.uangku.feature.backup.presentation.component.DisconnectGoogleDriveDialog
import com.example.uangku.feature.backup.presentation.component.RestoreDialog
import com.example.uangku.feature.google_drive.data.GoogleDriveAuth
import com.example.uangku.feature.period.presentation.PeriodDialog
import com.example.uangku.feature.period.presentation.PeriodEvent
import com.example.uangku.feature.period.presentation.PeriodViewModel
import com.example.uangku.feature.settings.presentation.component.SettingsItem
import com.example.uangku.feature.settings.presentation.component.exportLauncher
import com.example.uangku.feature.settings.presentation.component.googleDriveAuthLauncher
import com.example.uangku.feature.settings.presentation.component.importLauncher

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    periodViewModel: PeriodViewModel,
    backupViewModel: BackupViewModel,
    googleDriveAuth: GoogleDriveAuth
) {
    val context = LocalContext.current
    val periodState by periodViewModel.state.collectAsState()
    val backupState by backupViewModel.state.collectAsState()
    var showPeriodDialog by remember { mutableStateOf(false) }
    var showRestoreConfirmationDialog by remember { mutableStateOf(false) }
    var showDisconnectDriveDialog by remember { mutableStateOf(false) }
    val activity = context as Activity

    val exportLauncher = exportLauncher(backupViewModel)
    val importLauncher = importLauncher(backupViewModel)

    val googleDriveAuthorizationLauncher = googleDriveAuthLauncher(
        context = context,
        onAuthorizationCompleted = {
            backupViewModel.onEvent(BackupEvent.CheckAuth)
        }
    )

    LaunchedEffect(Unit) {
        periodViewModel.onEvent(PeriodEvent.onScreenOpen)

        backupViewModel.effect.collect { effect ->
            when (effect) {
                BackupEffect.LaunchExportFilePicker -> {
                    exportLauncher.launch("UangKu_backup.json")
                }

                BackupEffect.LaunchImportFilePicker -> {
                    importLauncher.launch(arrayOf("application/json"))
                }

                BackupEffect.RequestGoogleDriveAuthorization -> {
                    googleDriveAuth.authorize(
                        activity = activity,
                        onSuccess = { authorizationResult ->
                            Log.d(
                                "GOOGLE_DRIVE",
                                "Granted scopes = ${authorizationResult.grantedScopes}"
                            )

                            if (authorizationResult.hasResolution()) {
                                val pendingIntent = authorizationResult.pendingIntent
                                if (pendingIntent != null) {
                                    googleDriveAuthorizationLauncher.launch(
                                        IntentSenderRequest.Builder(
                                            pendingIntent.intentSender
                                        ).build()
                                    )
                                }
                            } else {
                                backupViewModel.onEvent(BackupEvent.CheckAuth)
                            }
                        },
                        onFailure = {
                            Toast.makeText(
                                context,
                                "Authorization gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(backupState.message) {
        backupState.message?.let { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
        backupViewModel.onEvent(BackupEvent.ClearMessage)
    }

    LaunchedEffect(backupState.bufferRestore) {
        if (backupState.bufferRestore != null) {
            showRestoreConfirmationDialog = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(title = "Pengaturan")
        },
        bottomBar = {
            UangKuNavigationBar(navController = navController, current = "settings")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // SEKSI 1: PENGATURAN KEUANGAN
            SectionLabel(text = "Pengaturan Keuangan")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = secondaryBgSkyBlue,
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    SettingsItem(
                        title = "Periode Keuangan",
                        description = "Atur tanggal awal periode",
                        trailingText = "Tgl ${periodState.selectedStartDay}",
                        icon = Icons.Default.DateRange,
                        iconBgColor = Color(0xFFE0F2FE),
                        iconTintColor = mainSkyBlue,
                        onClick = { showPeriodDialog = true }
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.padding(start = 68.dp)
                    )

                    SettingsItem(
                        title = "Kategori Transaksi",
                        description = "Kelola kategorimu",
                        icon = Icons.AutoMirrored.Filled.List,
                        iconBgColor = Color(0xFFFFF3E0),
                        iconTintColor = Color(0xFFFB6400),
                        onClick = {
                            navController.navigate("category")
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // SEKSI 2: PENCADANGAN & SINKRONISASI
            SectionLabel(text = "Pencadangan & Sinkronisasi")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = secondaryBgSkyBlue,
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    SettingsItem(
                        title = "Google Drive",
                        description = if (backupState.isGoogleDriveAuthorized) {
                            "Terhubung"
                        } else {
                            "Belum terhubung"
                        },
                        icon = if (backupState.isGoogleDriveAuthorized){
                            Icons.Default.Cloud
                        } else {
                            Icons.Default.CloudOff
                        },
                        iconBgColor = if (backupState.isGoogleDriveAuthorized) {
                            greenBg
                        } else {
                            redBg
                        },
                        iconTintColor = if (backupState.isGoogleDriveAuthorized) {
                            GreenComponent
                        } else {
                            redComponent
                        },
                        onClick = {
                            if (backupState.isGoogleDriveAuthorized) {
                                showDisconnectDriveDialog = true
                            } else {
                                backupViewModel.onEvent(BackupEvent.ConnectGoogleDrive)
                            }
                        }
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.padding(start = 68.dp)
                    )

                    AutoBackupSection(
                        state = backupState,
                        onEvent = backupViewModel::onEvent
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.padding(start = 68.dp)
                    )

                    SettingsItem(
                        title = "Ekspor Data Pencadangan",
                        description = "Simpan berkas pencadangan lokal",
                        icon = Icons.Default.Upload,
                        iconBgColor = Color(0xFFF3E8FF),
                        iconTintColor = Color(0xFF7E22CE),
                        onClick = { backupViewModel.onEvent(BackupEvent.PrepareExport) }
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.padding(start = 68.dp)
                    )

                    SettingsItem(
                        title = "Pulihkan Data",
                        description = "Muat data dari penyimpanan lokal & google drive",
                        icon = Icons.Default.Download,
                        iconBgColor = Color(0xFFE0E7FF),
                        iconTintColor = Color(0xFF4338CA),
                        onClick = { backupViewModel.onEvent(BackupEvent.PrepareImport) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // SEKSI 3: TENTANG APLIKASI
            SectionLabel(text = "Tentang Aplikasi")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = secondaryBgSkyBlue,
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                SettingsItem(
                    title = "UangKu",
                    description = "Aplikasi Catatan Keuangan Pribadi",
                    trailingText = "v1.0.0",
                    showChevron = false,
                    icon = Icons.Default.Info,
                    iconBgColor = Color(0xFFF1F5F9),
                    iconTintColor = Color(0xFF64748B),
                    onClick = {}
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        ShowPeriodDialog(
            showDialog = showPeriodDialog,
            selectedDate = periodState.selectedStartDay,
            onDismiss = { showPeriodDialog = false },
            onConfirm = { selected ->
                periodViewModel.onEvent(PeriodEvent.onStartDayChange(selected))
                periodViewModel.onEvent(PeriodEvent.onSave)
                showPeriodDialog = false
            }
        )

        RestoreDialog(
            showDialog = showRestoreConfirmationDialog,
            onDismiss = { showRestoreConfirmationDialog = false },
            onConfirm = {
                showRestoreConfirmationDialog = false
                backupViewModel.onEvent(BackupEvent.Restore)
            }
        )

        if (showDisconnectDriveDialog) {
            DisconnectGoogleDriveDialog(
                onDismiss = { showDisconnectDriveDialog = false },
                onConfirm = {
                    showDisconnectDriveDialog = false
                    backupViewModel.onEvent(BackupEvent.DisconnectGoogleDrive)
                }
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = 12.5.sp
        ),
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B),
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ShowPeriodDialog(
    showDialog: Boolean,
    selectedDate: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    if (showDialog) {
        PeriodDialog(
            selectedDate = selectedDate,
            onDismissDialog = onDismiss,
            onConfirmDialog = { selected ->
                onConfirm(selected)
            }
        )
    }
}
