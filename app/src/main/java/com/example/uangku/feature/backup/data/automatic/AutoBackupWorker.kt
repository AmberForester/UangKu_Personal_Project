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
        var isSuccess = false

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

            isSuccess = true
            Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Auto-backup gagal dieksekusi: ${e.message}", e)
            Result.failure()

        } finally {
            if (isEnabled) {
                if (isSuccess) {
                    // Berhasil: Jadwalkan untuk siklus normal berikutnya (misal: besok pukul 02:00)
                    Log.d(TAG, "Menjadwalkan auto-backup normal berikutnya (frekuensi: $frequency).")
                    autoBackupScheduler.scheduleNext(frequency = frequency)
                } else {
                    // Gagal di malam hari: Jadwalkan fallback pagi hari pukul 07:00
                    Log.w(TAG, "Gagal di malam hari, mengalihkan percobaan berikutnya ke pukul 07:00 pagi.")
                    autoBackupScheduler.scheduleMorningFallback()
                }
            }
        }
    }
}
