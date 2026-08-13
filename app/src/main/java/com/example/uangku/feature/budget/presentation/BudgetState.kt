package com.example.uangku.feature.budget.presentation

import com.example.uangku.feature.budget.domain.Budget
import com.example.uangku.feature.budget.domain.BudgetSummary

data class BudgetState (

    val budgets: List<BudgetSummary> = emptyList(),
    val selectedBudget: BudgetSummary? = null,
    val amount: String = "",

    val showDialog: Boolean = false,
    val errorMessage: String? = null

)