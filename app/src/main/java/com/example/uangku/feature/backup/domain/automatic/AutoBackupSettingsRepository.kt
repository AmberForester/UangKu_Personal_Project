package com.example.uangku.feature.backup.domain.automatic

import kotlinx.coroutines.flow.Flow

interface AutoBackupSettingsRepository {

    val settings: Flow<AutoBackupSettings>

    suspend fun setEnabled(
        enabled: Boolean
    )

    suspend fun setFrequency(
        frequency: BackupFrequency
    )
}