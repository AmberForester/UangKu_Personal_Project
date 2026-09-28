package com.example.uangku.feature.backup.presentation

sealed interface BackupEffect {

    data object  LaunchExportFilePicker: BackupEffect

    data object  LaunchImportFilePicker: BackupEffect

}