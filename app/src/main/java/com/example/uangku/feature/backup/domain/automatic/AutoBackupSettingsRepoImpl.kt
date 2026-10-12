package com.example.uangku.feature.backup.domain.automatic

import com.example.uangku.feature.backup.data.automatic.AutoBackupPreferences
import kotlinx.coroutines.flow.Flow

class AutoBackupSettingsRepositoryImpl(
    private val preferences: AutoBackupPreferences
) : AutoBackupSettingsRepository {

    override val settings: Flow<AutoBackupSettings>
        get() = preferences.settings

    override suspend fun setEnabled(
        enabled: Boolean
    ) {
        preferences.setEnabled(enabled)
    }

    override suspend fun setFrequency(
        frequency: BackupFrequency
    ) {
        preferences.setFrequency(frequency)
    }
}