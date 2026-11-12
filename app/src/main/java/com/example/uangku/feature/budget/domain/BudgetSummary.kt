package com.example.uangku.feature.budget.domain

data class BudgetSummary (

    val budgetId: Long?,
    val categoryId: Int,
    val categoryName: String,
    val budgetAmount: Double,
    val spent: Double,
    val transactionCount: Int

) {
    val remaining: Double
        get() = budgetAmount - spent

    val progress: Float
        get()  {

            if(budgetAmount <= 0.0)
                return 0f

            return (spent / budgetAmount).coerceIn(0.0, 1.0).toFloat()
        }

    val percentage: Int
        get() {
            if(budgetAmount <= 0.0) return 0
            return ((spent / budgetAmount) * 100).toInt()
        }

    val isOverBudget: Boolean
        get() = remaining < 0

}