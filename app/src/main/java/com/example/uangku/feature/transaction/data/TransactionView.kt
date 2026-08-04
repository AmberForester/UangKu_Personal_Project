package com.example.uangku.feature.transaction.data

import androidx.room.DatabaseView
import com.example.uangku.core.domain.Type
import java.util.Date

@DatabaseView(
    value = "SELECT " +
            "t.id, " +
            "t.description," +
            "t.type," +
            "t.amount," +
            "t.date, " +
            "t.categoryId, " +
            "c.name AS categoryName " +
            "FROM transactions t " +
            "INNER JOIN categories c " +
            "ON t.categoryId = c.id",

    viewName = "transaction_view"
)
data class TransactionView(
    val id: Long,
    val description: String,
    val type: Type,
    val amount: Double,

    val categoryId: Int,
    val categoryName: String,

    val date: Date
)
