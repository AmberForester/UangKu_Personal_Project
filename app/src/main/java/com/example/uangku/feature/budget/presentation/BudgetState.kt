package com.example.uangku.feature.budget.presentation

import com.example.uangku.feature.budget.domain.BudgetOverview
import com.example.uangku.feature.budget.domain.BudgetSummary

data class BudgetState (

    val budgets: List<BudgetSummary> = emptyList(),
    val selectedBudget: BudgetSummary? = null,
    val amount: String = "",

    val showDialog: Boolean = false,
    val errorMessage: String? = null,

    val availableAmount: Double = 0.0,

    val budgetOverview: BudgetOverview = BudgetOverview(
        income = 0.0,
        allocated = 0.0,
        available = 0.0
    )

)