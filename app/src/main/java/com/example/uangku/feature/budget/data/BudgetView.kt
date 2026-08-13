package com.example.uangku.feature.budget.data

import androidx.room.DatabaseView


@DatabaseView(
    value = "SELECT " +
            "b.id, " +
            "b.month," +
            "b.amount," +
            "b.year, " +
            "c.id AS categoryId, " +
            "c.name AS categoryName " +
            "FROM categories c " +
            "LEFT JOIN budgets b " +
            "ON c.id = b.categoryId ",

    viewName = "budget_view"
)
class BudgetView (

    val id: Long?,

    val categoryId: Int,

    val categoryName: String,

    val amount: Double?,

    val month: Int?,

    val year: Int?
)