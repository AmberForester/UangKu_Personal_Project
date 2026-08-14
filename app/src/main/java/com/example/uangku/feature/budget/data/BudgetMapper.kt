package com.example.uangku.feature.budget.data

import com.example.uangku.feature.budget.domain.Budget

fun BudgetView.toDomain(): Budget {
    return Budget(
        id = id,
        categoryId = categoryId,
        categoryName = categoryName,
        amount = amount,
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amount = amount,
        categoryId = categoryId,
    )
}