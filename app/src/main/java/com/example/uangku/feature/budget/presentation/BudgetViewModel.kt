package com.example.uangku.feature.budget.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.budget.domain.Budget
import com.example.uangku.feature.budget.domain.BudgetSummary
import com.example.uangku.feature.budget.domain.BudgetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetViewModel(

    private val budgetUseCase: BudgetUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state = _state.asStateFlow()

    fun onEvent(event: BudgetEvent) {
        when (event) {

            BudgetEvent.onScreenOpen -> {
                loadBudgets()
            }

            is BudgetEvent.onItemClick -> {
                openDialog(event.budget)
            }

            is BudgetEvent.onAmountChange -> {
                _state.update {
                    it.copy(
                        amount = event.amount,
                        errorMessage = null
                    )
                }
            }

            BudgetEvent.onDismissDialog -> {
                _state.update {
                    it.copy(
                        showDialog = false,
                        selectedBudget = null,
                        amount = "",
                        errorMessage = null,
                        availableAmount = 0.0
                    )
                }
            }

            BudgetEvent.onSaveClick -> {
                saveBudget()
            }

            BudgetEvent.onDismissError -> {
                _state.update {
                    it.copy(
                        errorMessage = null
                    )
                }
            }

            BudgetEvent.onReset -> {
                resetBudget()
            }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            budgetUseCase.getBudgetSummaries().collect { budgets ->
                _state.update {
                    it.copy(
                        budgets = budgets
                    )
                }
            }
        }

        viewModelScope.launch {
            budgetUseCase.getBudgetOverview().collect { overview ->
                _state.update {
                    it.copy(
                        budgetOverview = overview
                    )
                }
            }
        }
    }

    private fun saveBudget() {
        viewModelScope.launch {

            val selected = state.value.selectedBudget

            val budget = Budget(
                id = selected?.budgetId,
                amount = state.value.amount.toDouble(),
                categoryId = selected?.categoryId ?: 0,
                categoryName = selected?.categoryName ?: "",
            )

            Log.d(
                "SAVE_BUDGET",
                """
                id=${selected?.budgetId}
                categoryId=${selected?.categoryId}
                categoryName=${selected?.categoryName}
                amount=${state.value.amount}
                """.trimIndent()
            )

            try {
                budgetUseCase.saveBudget(budget)

                _state.update {
                    it.copy(
                        showDialog = false,
                        selectedBudget = null,
                        amount = ""
                    )
                }
            } catch (e: IllegalArgumentException) {

                _state.update {
                    it.copy(
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private fun openDialog(budget: BudgetSummary) {
        viewModelScope.launch {

            val availableAmount = budgetUseCase.getAvailableAmount(budget.budgetId)

            _state.update {
                it.copy(
                    selectedBudget = budget,
                    amount = budget.budgetAmount.toInt().toString(),
                    availableAmount = availableAmount,
                    errorMessage = null,
                    showDialog = true
                )
            }
        }
    }

    private fun resetBudget() {
        viewModelScope.launch {

            val selectedBudget = state.value.selectedBudget

            val budget = Budget(
                id = selectedBudget?.budgetId,
                amount = state.value.amount.toDouble(),
                categoryId = selectedBudget?.categoryId ?: 0,
                categoryName = selectedBudget?.categoryName ?: ""
            )
            budgetUseCase.deleteBudget(budget)

            _state.update {
                it.copy(
                    showDialog = false,
                    selectedBudget = null,
                    amount = ""
                )
            }
        }
    }
}