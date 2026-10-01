package com.example.uangku.feature.backup.data.manual

import android.content.ContentResolver
import android.net.Uri
import com.example.uangku.feature.backup.domain.FileManager

class FileManagerImpl(
    private val contentResolver: ContentResolver
) : FileManager {

    override fun write(uri: Uri, content: String) {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(content.toByteArray())
        } ?: throw IllegalStateException(
            "Tidak dapat membuka file untuk ditulis"
        )
    }

    override fun read(uri: Uri): String {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } ?: throw IllegalStateException(
            "Tidak dapat membuka file untuk dibaca"
        )
    }
}