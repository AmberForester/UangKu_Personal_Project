package com.example.uangku.feature.backup.data.automatic

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettings
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.autoBackupDataStore by preferencesDataStore(
    name = "auto_backup_preferences"
)

class AutoBackupPreferences(
    private val context: Context
) {

    companion object {
        private val ENABLED_KEY =
            booleanPreferencesKey("auto_backup_enabled")

        private val FREQUENCY_KEY =
            stringPreferencesKey("auto_backup_frequency")

        private val LAST_BACKUP_TIMESTAMP_KEY =
            longPreferencesKey("last_backup_timestamp")
    }

    val settings: Flow<AutoBackupSettings> =
        context.autoBackupDataStore.data.map { preferences ->

            val frequency = preferences[FREQUENCY_KEY]
                ?.let {
                    runCatching {
                        BackupFrequency.valueOf(it)
                    }.getOrDefault(
                        BackupFrequency.MONTHLY
                    )
                }
                ?: BackupFrequency.MONTHLY

            AutoBackupSettings(
                enabled = preferences[ENABLED_KEY] ?: false,
                frequency = frequency,
                lastBackupTimestamp = preferences[LAST_BACKUP_TIMESTAMP_KEY]
            )
        }

    suspend fun setEnabled(enabled: Boolean) {
        context.autoBackupDataStore.edit { preferences ->
            preferences[ENABLED_KEY] = enabled
        }
    }

    suspend fun setFrequency(
        frequency: BackupFrequency
    ) {
        context.autoBackupDataStore.edit { preferences ->
            preferences[FREQUENCY_KEY] = frequency.name
        }
    }

    suspend fun setLastBackupTimestamp(timestamp: Long) {
        context.autoBackupDataStore.edit { preferences ->
            preferences[LAST_BACKUP_TIMESTAMP_KEY] = timestamp
        }
    }
}