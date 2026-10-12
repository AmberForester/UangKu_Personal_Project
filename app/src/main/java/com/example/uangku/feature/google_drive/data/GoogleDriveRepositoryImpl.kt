package com.example.uangku.feature.google_drive.data

import android.content.Context
import com.example.uangku.feature.google_drive.domain.GoogleDriveRepository
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import java.util.Collections

class GoogleDriveRepositoryImpl (

    private val googleDriveAuth: GoogleDriveAuth,
    private val context: Context,

) : GoogleDriveRepository {

    companion object {
        private const val APPLICATION_NAME = "UangKu!"
        private const val BACKUP_FILE_NAME = "UangKu_AutoBackup.json"
        private const val MIME_TYPE = "application/json"
    }

    override suspend fun uploadBackup(
        json: String,
    ) {
        val accessToken = googleDriveAuth.getAccessToken(context)
        val drive = createDriveClient(accessToken)

        val existingFile = findBackupFile(drive)

        if (existingFile == null) {
            createBackupFile(drive, json)
        } else {
            updateBackupFile(
                drive = drive,
                fileId = existingFile.id,
                json = json
            )
        }
    }

    private fun createDriveClient(
        accessToken: String
    ): Drive {

        val credentials = GoogleCredentials
            .create(AccessToken(accessToken, null))
            .createScoped(
                Collections.singletonList(
                    "https://www.googleapis.com/auth/drive.file"
                )
            )

        val requestInitializer =
            HttpCredentialsAdapter(credentials)

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer
        )
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    private fun findBackupFile(
        drive: Drive
    ): File? {

        val result = drive.files()
            .list()
            .setQ(
                "name = '$BACKUP_FILE_NAME' " +
                        "and trashed = false"
            )
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()

        return result.files?.firstOrNull()
    }

    private fun createBackupFile(
        drive: Drive,
        json: String
    ) {

        val metadata = File().apply {
            name = BACKUP_FILE_NAME
            mimeType = MIME_TYPE
        }

        val mediaContent = ByteArrayContent(
            MIME_TYPE,
            json.toByteArray(Charsets.UTF_8)
        )

        drive.files()
            .create(metadata, mediaContent)
            .setFields("id, name")
            .execute()
    }

    private fun updateBackupFile(
        drive: Drive,
        fileId: String,
        json: String
    ) {

        val mediaContent = ByteArrayContent(
            MIME_TYPE,
            json.toByteArray(Charsets.UTF_8)
        )

        drive.files()
            .update(fileId, null, mediaContent)
            .setFields("id, name")
            .execute()
    }

    override suspend fun isAuthorized(): Boolean {
        return googleDriveAuth.isAuthorized(context)
    }

    override suspend fun disconnect() {
        googleDriveAuth.disconnect(context)
    }

}