package com.example.uangku.feature.backup.domain

import android.net.Uri

interface FileManager {

    fun write(
        uri: Uri,
        content: String
    )

    fun read(
        uri: Uri
    ): String
}