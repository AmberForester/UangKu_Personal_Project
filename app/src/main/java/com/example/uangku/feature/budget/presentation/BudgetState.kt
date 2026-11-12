package com.example.uangku.feature.budget.presentation

import com.example.uangku.feature.budget.domain.BudgetOverview
import com.example.uangku.feature.budget.domain.BudgetSummary
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.transaction.domain.Transaction

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
    ),

    // Detail Screen State
    val currentDetailBudget: BudgetSummary? = null,
    val detailTransactions: List<Transaction> = emptyList(),
    val currentPeriod: FinancialPeriod? = null,
    val isDetailLoading: Boolean = false

)