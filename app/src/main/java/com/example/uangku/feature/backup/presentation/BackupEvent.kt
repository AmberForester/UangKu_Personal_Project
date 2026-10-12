package com.example.uangku.feature.backup.presentation

import android.net.Uri
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency

sealed interface BackupEvent {

    data object PrepareExport: BackupEvent

    data class ExportFile(
        val uri: Uri
    ): BackupEvent

    data object PrepareImport: BackupEvent

    data class  ImportFile(
        val uri: Uri
    ): BackupEvent

    data object Restore: BackupEvent

    data object CancelRestore: BackupEvent

    data object ClearMessage: BackupEvent

    data object ConnectGoogleDrive : BackupEvent

    data object DisconnectGoogleDrive : BackupEvent

    data class EnableAutoBackup(
        val enable: Boolean
    ): BackupEvent

    data class SetAutoBackupFrequency(
        val frequency: BackupFrequency
    ): BackupEvent

    data object CheckAuth: BackupEvent

}