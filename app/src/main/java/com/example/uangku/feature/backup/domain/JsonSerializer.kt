package com.example.uangku.feature.backup.domain

import com.example.uangku.feature.backup.domain.manual.BackupData

interface JsonSerializer {

    fun toJson(data: BackupData): String

    fun fromJson(jsonString: String): BackupData

}