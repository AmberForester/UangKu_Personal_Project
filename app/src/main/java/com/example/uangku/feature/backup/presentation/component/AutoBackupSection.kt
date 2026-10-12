package com.example.uangku.feature.backup.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency
import com.example.uangku.feature.backup.presentation.BackupEvent
import com.example.uangku.feature.backup.presentation.BackupState

@Composable
fun AutoBackupSection(
    state: BackupState,
    onEvent: (BackupEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)

    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Top)
            )  {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = "icon",
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }

            Spacer(Modifier.padding(5.dp))

            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Pencadangan Otomatis",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp
                )

                if(!state.isGoogleDriveAuthorized) {
                    Text(
                        text = "Google Drive belum terhubung",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    Text(
                        text = if (state.autoBackupSettings.enabled) {
                            "Aktif"
                        } else {
                            "Tidak aktif"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if(state.isGoogleDriveAuthorized) {
                Spacer(Modifier.padding(5.dp))
                Switch(
                    checked = state.autoBackupSettings.enabled,
                    onCheckedChange = { enabled ->
                        onEvent(
                            BackupEvent.EnableAutoBackup(
                                enable = enabled
                            )
                        )
                    },
                    modifier = Modifier.weight(0.2f)
                )
            }
        }

        if (state.autoBackupSettings.enabled) {

            Text(
                text = "Frekuensi Pencadangan",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    top = 10.dp,
                )
            )

            BackupFrequency.entries.forEach { frequency ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(
                                BackupEvent.SetAutoBackupFrequency(
                                    frequency = frequency
                                )
                            )
                        }
//                        .padding(vertical = 5.dp),
                            ,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected =
                        state.autoBackupSettings.frequency == frequency,
                        onClick = {
                            onEvent(
                                BackupEvent.SetAutoBackupFrequency(
                                    frequency = frequency
                                )
                            )
                        }
                    )

                    Text(
                        text = frequency.toDisplayName(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun BackupFrequency.toDisplayName(): String {
    return when (this) {
        BackupFrequency.DAILY -> "Harian"
        BackupFrequency.WEEKLY -> "Mingguan"
        BackupFrequency.MONTHLY -> "Bulanan"
    }
}