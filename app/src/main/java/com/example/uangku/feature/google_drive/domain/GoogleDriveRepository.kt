package com.example.uangku.feature.google_drive.domain

interface GoogleDriveRepository {

    suspend fun uploadBackup(json: String)

    suspend fun isAuthorized(): Boolean

    suspend fun disconnect()

}