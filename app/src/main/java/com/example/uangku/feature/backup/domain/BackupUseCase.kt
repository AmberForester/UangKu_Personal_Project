package com.example.uangku.feature.backup.domain

class BackupUseCase(
    private val repository: BackupRepository
) {
    suspend fun getBackupData(): BackupData {
        return repository.getBackupData()
    }

    suspend fun restoreData(
        data: BackupData
    ) {
        repository.restoreData(data)
    }
}