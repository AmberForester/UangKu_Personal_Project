package com.example.uangku.feature.transaction.domain

import com.example.uangku.core.domain.Type
import java.util.Date

data class Transaction(
    val id: Long? = null,
    val description: String,
    val type: Type,
    val amount: Double,

    val categoryId: Int,
    val categoryName: String,

    val date: Date
)