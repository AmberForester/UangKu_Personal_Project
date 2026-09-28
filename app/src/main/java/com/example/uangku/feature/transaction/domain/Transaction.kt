package com.example.uangku.feature.transaction.domain

import com.example.uangku.core.database.DateSerializer
import com.example.uangku.core.domain.Type
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Transaction(
    val id: Long? = null,
    val description: String,
    val type: Type,
    val amount: Double,

    val categoryId: Int,
    val categoryName: String,

    @Serializable(with = DateSerializer::class)
    val date: Date
)