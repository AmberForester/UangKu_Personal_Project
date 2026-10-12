package com.example.uangku.feature.backup.domain.automatic

import com.example.uangku.feature.google_drive.domain.GoogleDriveRepository
import com.example.uangku.feature.backup.domain.manual.BackupUseCase

class AutoBackupUseCase (
    private val backupUseCase: BackupUseCase,
    private val googleDriveRepository: GoogleDriveRepository,
){
    suspend operator fun invoke(){

        val backupData = backupUseCase.getBackupData()

        val json = backupUseCase.convertToJson(backupData)

        googleDriveRepository.uploadBackup(json)
    }
}