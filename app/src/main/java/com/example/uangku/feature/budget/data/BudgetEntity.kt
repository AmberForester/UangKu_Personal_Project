package com.example.uangku.feature.budget.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.uangku.feature.category.data.CategoryEntity

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class BudgetEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val amount: Double?,

    val categoryId: Int,

)