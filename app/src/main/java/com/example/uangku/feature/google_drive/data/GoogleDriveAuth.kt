package com.example.uangku.feature.google_drive.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.ClearTokenRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class GoogleDriveAuth {
    companion object {
        const val DRIVE_FILE_SCOPE =
            "https://www.googleapis.com/auth/drive.file"
    }

    private fun getAuthorizationRequest(): AuthorizationRequest {
        return AuthorizationRequest.builder()
            .setRequestedScopes(
                listOf(
                    Scope(DRIVE_FILE_SCOPE),
                )
            )
            .build()
    }

    fun authorize(
        activity: Activity,
        onSuccess: (AuthorizationResult) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        Identity.getAuthorizationClient(activity)
            .authorize(getAuthorizationRequest())
            .addOnSuccessListener { result ->
                onSuccess(result)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getAuthorizationResultFromIntent(
        activity: Activity,
        intent: Intent
    ): AuthorizationResult {
        return Identity
            .getAuthorizationClient(activity)
            .getAuthorizationResultFromIntent(intent)
    }

    suspend fun getAccessToken(
        context: Context
    ): String {
        val result = Identity
            .getAuthorizationClient(context)
            .authorize(getAuthorizationRequest())
            .await()

        if (result.hasResolution()) {
            throw IllegalStateException(
                "Google Drive belum diotorisasi"
            )
        }

        return result.accessToken
            ?: throw IllegalStateException(
                "Access token Google Drive tidak tersedia"
            )
    }

    suspend fun isAuthorized(context: Context): Boolean {
        return try {
            val result = Identity
                .getAuthorizationClient(context)
                .authorize(getAuthorizationRequest())
                .await()

            !result.hasResolution() &&
                    result.accessToken != null
        } catch (e: Exception) {
            false
        }
    }
    suspend fun disconnect(context: Context) {
        val client = Identity.getAuthorizationClient(context)

        // 1. Ambil sesi berjalan → butuh token untuk dicabut
        val result = client.authorize(getAuthorizationRequest()).await()

        if (result.hasResolution()) {
            throw IllegalStateException("Tidak ada sesi Google Drive yang bisa dicabut")
        }

        val token = result.accessToken
            ?: throw IllegalStateException("Token tidak tersedia")

        // 2. REVOKE — cabut izin langsung di server Google OAuth 2.0 (standar RFC 7009)
        // Cara ini tidak memerlukan objek Account dan tidak crash di GMS Play Services.
        // Jika gagal (misal offline), akan melempar exception sehingga state TIDAK diubah.
        revokeTokenOnGoogleServer(token)

        // 3. Bersihkan token dari cache lokal Play Services
        try {
            client.clearToken(
                ClearTokenRequest
                    .builder()
                    .setToken(token)
                    .build()
            ).await()
        } catch (e: Exception) {
            Log.e(
                "GOOGLE_DRIVE",
                "clearToken gagal (diabaikan)",
                e
            )
        }
    }

    private suspend fun revokeTokenOnGoogleServer(token: String) = withContext(Dispatchers.IO) {
        val url = URL("https://oauth2.googleapis.com/revoke?token=$token")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 10000
            readTimeout = 10000
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            doOutput = true
        }

        try {
            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                val errorStream = connection.errorStream?.bufferedReader()?.use { it.readText() }
                throw IOException("Gagal mencabut izin di Google (HTTP $responseCode): $errorStream")
            }
        } finally {
            connection.disconnect()
        }
    }
}