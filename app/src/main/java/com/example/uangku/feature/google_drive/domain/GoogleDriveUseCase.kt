package com.example.uangku.feature.google_drive.domain

class GoogleDriveUseCase (

    private val googleDriveRepository: GoogleDriveRepository,

    ) {

    suspend fun isAuthorized(
    ): Boolean {
        return googleDriveRepository.isAuthorized()
    }

    suspend fun disconnect() {
        googleDriveRepository.disconnect()
    }

}