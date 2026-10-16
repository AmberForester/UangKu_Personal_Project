package com.example.uangku.feature.backup.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency
import com.example.uangku.feature.backup.presentation.BackupEvent
import com.example.uangku.feature.backup.presentation.BackupState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AutoBackupSection(
    state: BackupState,
    onEvent: (BackupEvent) -> Unit
) {
    val context = LocalContext.current
    var showBatteryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Baris Utama: Icon, Judul & Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE0F2FE),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = mainSkyBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Pencadangan Otomatis",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A)
                )

                Spacer(Modifier.height(2.dp))

                if (!state.isGoogleDriveAuthorized) {
                    Text(
                        text = "Google Drive belum terhubung",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = generalRed
                    )
                } else {
                    val statusText = if (state.autoBackupSettings.enabled) "Aktif" else "Tidak aktif"
                    val lastBackup = formatLastBackupTime(state.autoBackupSettings.lastBackupTimestamp)

                    Text(
                        text = if (lastBackup.isNotEmpty()) "$statusText • $lastBackup" else statusText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = if (state.autoBackupSettings.enabled) successGreen else Color(0xFF64748B)
                    )
                }
            }

            if (state.isGoogleDriveAuthorized) {
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = state.autoBackupSettings.enabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (!isBatteryOptimizationIgnored(context)) {
                                showBatteryDialog = true
                            } else {
                                onEvent(BackupEvent.EnableAutoBackup(enable = true))
                            }
                        } else {
                            onEvent(BackupEvent.EnableAutoBackup(enable = false))
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = mainSkyBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    )
                )
            }
        }

        // Pilihan Frekuensi Backup (Radio Button) jika aktif
        if (state.isGoogleDriveAuthorized && state.autoBackupSettings.enabled) {
            HorizontalDivider(
                color = Color(0xFFF1F5F9),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 12.dp)
            ) {
                Text(
                    text = "Frekuensi Pencadangan",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 12.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                BackupFrequency.entries.forEach { frequency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(BackupEvent.SetAutoBackupFrequency(frequency = frequency))
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = frequency == state.autoBackupSettings.frequency,
                            onClick = {
                                onEvent(BackupEvent.SetAutoBackupFrequency(frequency = frequency))
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = mainSkyBlue,
                                unselectedColor = Color(0xFF94A3B8)
                            )
                        )

                        Spacer(Modifier.width(8.dp))

                        val frequencyLabel = when (frequency) {
                            BackupFrequency.DAILY -> "Harian"
                            BackupFrequency.WEEKLY -> "Mingguan (Setiap minggu)"
                            BackupFrequency.MONTHLY -> "Bulanan (Setiap bulan)"
                        }

                        Text(
                            text = frequencyLabel,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.5.sp
                            ),
                            color = Color(0xFF1E293B)
                        )
                    }
                }
            }
        }
    }

    if (showBatteryDialog) {
        BatteryOptimizationDialog(
            onDismiss = {
                showBatteryDialog = false
                // Tetap aktifkan jika pengguna memilih nanti saja
                onEvent(BackupEvent.EnableAutoBackup(enable = true))
            },
            onConfirm = {
                showBatteryDialog = false
                onEvent(BackupEvent.EnableAutoBackup(enable = true))
            }
        )
    }
}

fun formatLastBackupTime(timestamp: Long?): String {
    if (timestamp == null || timestamp == 0L) return ""

    val backupCalendar = Calendar.getInstance().apply { timeInMillis = timestamp }
    val nowCalendar = Calendar.getInstance()

    val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
    val time = timeFormat.format(Date(timestamp))

    val isToday = nowCalendar.get(Calendar.YEAR) == backupCalendar.get(Calendar.YEAR) &&
            nowCalendar.get(Calendar.DAY_OF_YEAR) == backupCalendar.get(Calendar.DAY_OF_YEAR)

    val yesterdayCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterdayCalendar.get(Calendar.YEAR) == backupCalendar.get(Calendar.YEAR) &&
            yesterdayCalendar.get(Calendar.DAY_OF_YEAR) == backupCalendar.get(Calendar.DAY_OF_YEAR)

    return when {
        isToday -> "Hari ini, $time"
        isYesterday -> "Kemarin, $time"
        else -> {
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
            dateFormat.format(Date(timestamp))
        }
    }
}
