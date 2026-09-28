package com.example.uangku.feature.backup.presentation

import android.net.Uri

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

}