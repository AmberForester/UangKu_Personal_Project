package com.example.uangku.feature.backup.data

import android.content.ContentResolver
import android.net.Uri

class FileManager(
    private val contentResolver: ContentResolver
) {

    fun write(uri: Uri, content: String) {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(content.toByteArray())
        } ?: throw IllegalStateException(
            "Tidak dapat membuka file untuk ditulis"
        )
    }

    fun read(uri: Uri): String {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } ?: throw IllegalStateException(
            "Tidak dapat membuka file untuk dibaca"
        )
    }
}