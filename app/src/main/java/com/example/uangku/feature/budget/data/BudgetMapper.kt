package com.example.uangku.feature.budget.data

import com.example.uangku.feature.budget.domain.Budget

fun BudgetView.toDomain(): Budget {
    return Budget(
        id = id,
        categoryId = categoryId,
        categoryName = categoryName,
        amount = amount,
        month = month,
        year = year,
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amount = amount,
        categoryId = categoryId,
        month = month!!,
        year = year!!,
    )
}