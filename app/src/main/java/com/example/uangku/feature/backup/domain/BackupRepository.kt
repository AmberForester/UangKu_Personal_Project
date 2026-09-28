package com.example.uangku.feature.backup.domain

interface BackupRepository {

    suspend fun getBackupData(): BackupData

    suspend fun restoreData(backupData: BackupData)

}