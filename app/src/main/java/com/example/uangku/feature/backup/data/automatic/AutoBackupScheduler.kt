package com.example.uangku.feature.backup.data.automatic

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
        private const val FALLBACK_HOUR = 7
        private const val FALLBACK_MINUTE = 0
    }

    fun scheduleNext(
        frequency: BackupFrequency
    ) {
        val nextRun = calculateNextRun(frequency)
        enqueueWork(nextRun)
    }

    /**
     * Menjadwalkan percobaan cadangan pagi hari (pukul 07:00)
     * jika eksekusi dini hari (pukul 02:00) gagal karena Doze Mode / jaringan mati.
     */
    fun scheduleMorningFallback() {
        val now = LocalDateTime.now()
        val morningTarget = now.with(LocalTime.of(FALLBACK_HOUR, FALLBACK_MINUTE))

        val nextRun = if (now.isBefore(morningTarget)) {
            morningTarget
        } else {
            // Jika jam 7 pagi sudah lewat, tetap tunda sedikit (30 menit) agar tidak membebani sistem
            now.plusMinutes(30)
        }

        enqueueWork(nextRun)
    }

    fun cancel() {
        WorkManager
            .getInstance(context)
            .cancelUniqueWork(WORK_NAME)
    }

    private fun enqueueWork(targetTime: LocalDateTime) {
        val delay = Duration.between(
            LocalDateTime.now(),
            targetTime
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
                if (now.isBefore(targetToday)) {
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
