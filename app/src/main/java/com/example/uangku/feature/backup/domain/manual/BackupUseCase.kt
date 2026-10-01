package com.example.uangku.feature.backup.domain.manual

import android.net.Uri
import com.example.uangku.feature.backup.domain.FileManager
import com.example.uangku.feature.backup.domain.JsonSerializer

class BackupUseCase(
    private val backupRepository: BackupRepository,
    private val serializer: JsonSerializer,
    private val fileManager: FileManager
) {
    suspend fun getBackupData(): BackupData {
        return backupRepository.getBackupData()
    }

    suspend fun restoreData(
        data: BackupData
    ) {
        backupRepository.restoreData(data)
    }

    fun convertToJson(backupData: BackupData): String {

        return serializer.toJson(backupData)

    }

    fun convertFromJson(jsonString: String): BackupData {

        return serializer.fromJson(jsonString)

    }

    fun write(uri: Uri, content: String) {

        return fileManager.write(uri, content)

    }

    fun read(uri: Uri): String {

        return fileManager.read(uri)

    }

}