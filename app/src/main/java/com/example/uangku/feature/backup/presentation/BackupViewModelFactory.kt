package com.example.uangku.feature.backup.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettingsUseCase
import com.example.uangku.feature.backup.domain.manual.BackupUseCase
import com.example.uangku.feature.google_drive.domain.GoogleDriveUseCase

class BackupViewModelFactory (

    private val backupUseCase: BackupUseCase,
    private val autoBackupSettingsUseCase: AutoBackupSettingsUseCase,
    private val googleDriveUseCase: GoogleDriveUseCase

    ) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
            return BackupViewModel(
                backupUseCase = backupUseCase,
                autoBackupSettingsUseCase = autoBackupSettingsUseCase,
                googleDriveUseCase = googleDriveUseCase,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}