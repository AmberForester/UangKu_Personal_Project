package com.example.uangku.feature.transaction.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.data.CategoryEntity
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class TransactionEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val description: String,
    val type: Type,
    val amount: Double,

    val categoryId: Int,

    val date: Date
)