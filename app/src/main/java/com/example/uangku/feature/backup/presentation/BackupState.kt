package com.example.uangku.feature.backup.presentation

import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettings
import com.example.uangku.feature.backup.domain.manual.BackupData

data class BackupState(

    val message: String? = null,
    val exportJson: String? = null,
    val bufferRestore: BackupData? = null,
    val autoBackupSettings: AutoBackupSettings = AutoBackupSettings(),
    val isGoogleDriveAuthorized: Boolean = false

)
