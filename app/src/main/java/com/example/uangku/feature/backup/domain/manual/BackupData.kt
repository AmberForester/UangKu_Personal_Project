package com.example.uangku.feature.backup.domain.manual

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingLog
import com.example.uangku.feature.transaction.domain.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class BackupData (

    val version: Int = 2,
    val categories: List<Category>,
    val transactions: List<Transaction>,
    val savingGoals: List<SavingGoal> = emptyList(),
    val savingLogs: List<SavingLog> = emptyList()

)