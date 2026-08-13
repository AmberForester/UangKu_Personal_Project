package com.example.uangku.feature.budget.presentation

import com.example.uangku.feature.budget.data.BudgetEntity
import com.example.uangku.feature.budget.domain.Budget
import com.example.uangku.feature.budget.domain.BudgetSummary

interface BudgetEvent {

    data object onScreenOpen: BudgetEvent

    data class onItemClick(
        val budget: BudgetSummary
    ) : BudgetEvent

    data class onAmountChange(
        val amount: String
    ) : BudgetEvent

    data object onSaveClick: BudgetEvent

    data object onDismissDialog: BudgetEvent

    data object onDismissError : BudgetEvent

}