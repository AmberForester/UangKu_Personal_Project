package com.example.uangku.feature.backup.domain.manual

interface BackupRepository {

    suspend fun getBackupData(): BackupData

    suspend fun restoreData(backupData: BackupData)

}