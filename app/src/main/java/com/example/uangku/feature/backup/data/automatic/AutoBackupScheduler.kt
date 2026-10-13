package com.example.uangku.feature.backup.data.automatic

import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class AutoBackupScheduler(
    private val context: Context
) {

    companion object {
        private const val WORK_NAME = "automatic_backup"
        private const val BACKUP_HOUR = 2
        private const val BACKUP_MINUTE = 0
    }

    fun scheduleNext(
        frequency: BackupFrequency
    ) {
        val nextRun = calculateNextRun(frequency)

        val delay = Duration.between(
            LocalDateTime.now(),
            nextRun
        ).toMillis()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()

        val workRequest =
            OneTimeWorkRequestBuilder<AutoBackupWorker>()
                .setInitialDelay(
                    delay.coerceAtLeast(0),
                    TimeUnit.MILLISECONDS
                )
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun cancel() {
        WorkManager
            .getInstance(context)
            .cancelUniqueWork(WORK_NAME)
    }

    private fun calculateNextRun(
        frequency: BackupFrequency
    ): LocalDateTime {

        val now = LocalDateTime.now()
        val backupTime = LocalTime.of(
            BACKUP_HOUR,
            BACKUP_MINUTE
        )

        return when (frequency) {

            BackupFrequency.DAILY -> {
                val targetToday = now.with(backupTime)
                if(now.isBefore(targetToday)) {
                    targetToday
                } else {
                    targetToday.plusDays(1)
                }
            }

            BackupFrequency.WEEKLY -> {
                now
                    .plusWeeks(1)
                    .with(backupTime)
            }

            BackupFrequency.MONTHLY -> {
                now
                    .with(
                        TemporalAdjusters.firstDayOfNextMonth()
                    )
                    .with(backupTime)
            }
        }
    }
}