package com.example.uangku.feature.backup.domain.automatic

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.backup.data.automatic.AutoBackupScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@RequiresApi(Build.VERSION_CODES.O)
class AutoBackupSettingsUseCase(
    private val repository: AutoBackupSettingsRepository,
    private val scheduler: AutoBackupScheduler
) {

    val settings: Flow<AutoBackupSettings>
        get() = repository.settings

    suspend fun setEnabled(
        enabled: Boolean
    ) {
        repository.setEnabled(enabled)

        if (enabled) {
            val settings = repository.settings.first()

            scheduler.scheduleNext(
                frequency = settings.frequency
            )
        } else {
            scheduler.cancel()
        }
    }

    suspend fun setFrequency(
        frequency: BackupFrequency
    ) {
        repository.setFrequency(frequency)

        val settings = repository.settings.first()

        if (settings.enabled) {
            scheduler.scheduleNext(
                frequency = settings.frequency
            )
        }
    }
}