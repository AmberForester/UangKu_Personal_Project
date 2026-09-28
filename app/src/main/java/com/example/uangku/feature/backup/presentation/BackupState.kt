package com.example.uangku.feature.backup.presentation

import com.example.uangku.feature.backup.domain.BackupData

data class BackupState(

    val message: String? = null,
    val exportJson: String? = null,
    val bufferRestore: BackupData? = null

)
