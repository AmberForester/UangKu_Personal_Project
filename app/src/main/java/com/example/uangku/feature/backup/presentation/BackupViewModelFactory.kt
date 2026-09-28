package com.example.uangku.feature.backup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.backup.data.FileManager
import com.example.uangku.feature.backup.data.JsonSerializer
import com.example.uangku.feature.backup.domain.BackupUseCase

class BackupViewModelFactory (

    private val backupUseCase: BackupUseCase,
    private val jsonSerializer: JsonSerializer,
    private val fileManager: FileManager,

) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
            return BackupViewModel(backupUseCase, jsonSerializer, fileManager) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}