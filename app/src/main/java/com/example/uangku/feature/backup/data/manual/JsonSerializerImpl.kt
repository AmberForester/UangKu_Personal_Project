package com.example.uangku.feature.backup.data.manual

import com.example.uangku.feature.backup.domain.JsonSerializer
import com.example.uangku.feature.backup.domain.manual.BackupData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class JsonSerializerImpl : JsonSerializer {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override fun toJson(data: BackupData): String {
        return json.encodeToString(data)
    }

    override fun fromJson(jsonString: String): BackupData {
        return json.decodeFromString(jsonString)
    }

}