package com.example.uangku.feature.settings.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.google_drive.data.GoogleDriveAuth
import com.example.uangku.feature.backup.presentation.BackupEffect
import com.example.uangku.feature.backup.presentation.BackupEvent
import com.example.uangku.feature.backup.presentation.BackupViewModel
import com.example.uangku.feature.backup.presentation.component.AutoBackupSection
import com.example.uangku.feature.backup.presentation.component.RestoreDialog
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
    val activity = context as Activity

//    val exportLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.CreateDocument(
//            "application/json"
//        )
//    ) { uri ->
//        Log.d("BACK UP JSON: ", "launcher activated")
//        if (uri != null) {
//            backupViewModel.onEvent(
//                BackupEvent.ExportFile(uri)
//            )
//        }
//    }
    val exportLauncher = exportLauncher(backupViewModel)

//    val importLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenDocument()
//    ) { uri ->
//        if(uri != null){
//            backupViewModel.onEvent(
//                BackupEvent.ImportFile(uri)
//            )
//        }
//    }
    val importLauncher = importLauncher(backupViewModel)

//    val googleDriveAuthorizationLauncher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.StartIntentSenderForResult()
//        ) { activityResult ->
//            try {
//                val authorizationResult =
//                    Identity.getAuthorizationClient(context)
//                        .getAuthorizationResultFromIntent(activityResult.data)
//
//                Toast.makeText(
//                    context,
//                    "Google Drive berhasil terhubung!",
//                    Toast.LENGTH_LONG
//                ).show()
//            } catch (e: ApiException) {
//                Toast.makeText(
//                    context,
//                    "Gagal menghubungkan Google Drive",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
    val googleDriveAuthorizationLauncher = googleDriveAuthLauncher(
        context = context,
        onAuthorizationCompleted = {
            backupViewModel.onEvent(BackupEvent.CheckAuth)
        }
    )

    LaunchedEffect(Unit) {
        periodViewModel.onEvent(PeriodEvent.onScreenOpen)

        backupViewModel.effect.collect { effect ->
            when(effect){

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

                                val pendingIntent =
                                    authorizationResult.pendingIntent

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
                        onFailure = { exception ->
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
        if(backupState.bufferRestore != null){
            showRestoreConfirmationDialog = true
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(title = "Settings")
        },
        bottomBar = { UangKuNavigationBar(navController = navController, current = "settings") }

    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ){
            Spacer(Modifier.padding(20.dp))
            SettingsItem(
                title = "Financial Period",
                description = "",
                icon = Icons.Default.Timeline,
                onClick = { showPeriodDialog = true }
            )

            SettingsItem(
                title = "Category",
                description = "",
                icon = Icons.AutoMirrored.Filled.List,
                onClick = {
                    navController.navigate("category")
                }
            )

            SettingsItem(
                title = "Export Data",
                description = "",
                icon = Icons.Default.Backup,
                onClick = { backupViewModel.onEvent(BackupEvent.PrepareExport) }
            )

            SettingsItem(
                title = "Import Data",
                description = "",
                icon = Icons.Default.Restore,
                onClick = { backupViewModel.onEvent(BackupEvent.PrepareImport) }
            )

            SettingsItem(
                title = "Google Drive",
                description = if (backupState.isGoogleDriveAuthorized) {
                    "Terhubung"
                } else {
                    "Belum terhubung"
                },
                icon = Icons.Default.Cloud,
                onClick = {
                    if (backupState.isGoogleDriveAuthorized) {
                        backupViewModel.onEvent(BackupEvent.DisconnectGoogleDrive)
                    } else {
                        backupViewModel.onEvent(BackupEvent.ConnectGoogleDrive)
                    }
                }
            )

            AutoBackupSection(
                state = backupState,
                onEvent = backupViewModel::onEvent
            )
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
    }
}

@Composable
fun ShowPeriodDialog(

    showDialog: Boolean,
    selectedDate: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit

) {
    if(showDialog){
        PeriodDialog(
            selectedDate = selectedDate,
            onDismissDialog = onDismiss,
            onConfirmDialog = { selected ->
                onConfirm(selected)
            }
        ) 
    }
}