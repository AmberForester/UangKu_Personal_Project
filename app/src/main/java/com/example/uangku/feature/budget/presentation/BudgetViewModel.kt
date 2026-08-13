package com.example.uangku.feature.budget.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.budget.domain.Budget
import com.example.uangku.feature.budget.domain.BudgetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class BudgetViewModel(

    private val budgetUseCase: BudgetUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state = _state.asStateFlow()

    fun onEvent(event: BudgetEvent){
        when(event){

            BudgetEvent.onScreenOpen -> {
                loadBudgets()
            }

            is BudgetEvent.onItemClick -> {
                _state.update {
                    it.copy(
                        selectedBudget = event.budget,
                        amount = event.budget.budgetAmount.toInt().toString(),
                        showDialog = true
                    )
                }
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
                        showDialog = false
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
    }

    private fun saveBudget(){
        viewModelScope.launch {

            val selected = state.value.selectedBudget

            val calendar = Calendar.getInstance()

            val budget = Budget(
                id = selected?.budgetId,
                amount = state.value.amount.toDouble(),
                categoryId = selected?.categoryId ?: 0,
                categoryName = selected?.categoryName ?: "",
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR),
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

            try{
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
}