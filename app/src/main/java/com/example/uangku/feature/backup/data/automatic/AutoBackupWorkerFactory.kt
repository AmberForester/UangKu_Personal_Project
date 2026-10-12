package com.example.uangku.feature.backup.data.automatic

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.uangku.feature.backup.domain.automatic.AutoBackupUseCase

@RequiresApi(Build.VERSION_CODES.O)
class AutoBackupWorkerFactory(
    private val autoBackupUseCase: AutoBackupUseCase,
    private val autoBackupPreferences: AutoBackupPreferences,
    private val autoBackupScheduler: AutoBackupScheduler
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {

            AutoBackupWorker::class.java.name -> {
                AutoBackupWorker(
                    context = appContext,
                    workerParameters = workerParameters,
                    autoBackupUseCase = autoBackupUseCase,
                    autoBackupPreferences = autoBackupPreferences,
                    autoBackupScheduler = autoBackupScheduler,
                )
            }
            else -> null
        }
    }
}
