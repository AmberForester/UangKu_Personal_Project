package com.example.uangku.feature.backup.domain.automatic

data class AutoBackupSettings(

    val enabled: Boolean = false,
    val frequency: BackupFrequency = BackupFrequency.MONTHLY

)
