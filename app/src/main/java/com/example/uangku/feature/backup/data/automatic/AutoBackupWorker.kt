package com.example.uangku.feature.backup.data.automatic

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.uangku.feature.backup.domain.automatic.AutoBackupUseCase
import com.example.uangku.feature.backup.domain.automatic.BackupFrequency
import kotlinx.coroutines.flow.first

@RequiresApi(Build.VERSION_CODES.O)
class AutoBackupWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val autoBackupUseCase: AutoBackupUseCase,
    private val autoBackupPreferences: AutoBackupPreferences,
    private val autoBackupScheduler: AutoBackupScheduler
) : CoroutineWorker(
    context,
    workerParameters
) {

    companion object {
        private const val TAG = "AUTO_BACKUP"
    }

    override suspend fun doWork(): Result {
        var isEnabled = false
        var frequency = BackupFrequency.DAILY

        return try {
            val settings = autoBackupPreferences.settings.first()
            isEnabled = settings.enabled
            frequency = settings.frequency

            if (!isEnabled) {
                Log.d(TAG, "Auto-backup tidak aktif, worker dilewati.")
                return Result.success()
            }

            Log.d(TAG, "Memulai proses auto-backup ke Google Drive...")
            autoBackupUseCase()
            autoBackupPreferences.setLastBackupTimestamp(System.currentTimeMillis())
            Log.i(TAG, "Auto-backup berhasil diunggah ke Google Drive.")

            Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Auto-backup gagal dieksekusi: ${e.message}", e)
            Result.failure()

        } finally {
            // Pastikan jadwal berikutnya tetap terpasang agar rantai auto-backup tidak putus
            if (isEnabled) {
                Log.d(TAG, "Menjadwalkan auto-backup berikutnya (frekuensi: $frequency).")
                autoBackupScheduler.scheduleNext(frequency = frequency)
            }
        }
    }
}