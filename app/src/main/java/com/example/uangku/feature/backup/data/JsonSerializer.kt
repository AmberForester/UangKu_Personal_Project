package com.example.uangku.feature.backup.data

import com.example.uangku.feature.backup.domain.BackupData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class JsonSerializer {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun toJson(backupData: BackupData): String {
        return json.encodeToString(backupData)
    }

    fun fromJson(jsonString: String): BackupData {
        return json.decodeFromString(jsonString)
    }

}