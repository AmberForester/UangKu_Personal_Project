package com.example.uangku.feature.dashboard.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class DashboardViewModel(

    private val periodUseCase: PeriodUseCase,
    private val transactionUseCase: TransactionUseCase,
    private val budgetUseCase: BudgetUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())

    val state = _state.asStateFlow()

    fun onEvent(event: DashboardEvent){

        when(event){

            DashboardEvent.onScreenOpen -> {
                loadFinancialPeriod()
                loadMonthlySummary()
                loadBudgetSummaries()
                loadRecentTransactions()
                Log.d("this month", _state.value.month.toString())
            }
        }
    }

    private fun loadFinancialPeriod() {

        viewModelScope.launch {

            val period = periodUseCase.getCurrentFinancialPeriod()

            _state.update {
                it.copy(
                    financialPeriod = period
                )
            }
        }
    }

    private fun loadMonthlySummary() {

        viewModelScope.launch {

            transactionUseCase.getMonthlySummary(_state.value.month).collect { summary ->
                _state.update {
                    it.copy(
                        summary = summary
                    )
                }
            }
        }
    }

    private fun loadBudgetSummaries() {
        viewModelScope.launch {
            budgetUseCase.getDashboardBudget().collect { summaries ->
                _state.update {
                    it.copy(
                        budgets = summaries
                    )
                }
            }
        }
    }

    private fun loadRecentTransactions(){
        viewModelScope.launch {
            transactionUseCase.getRecentTransactions().collect { transactions ->
                _state.update {
                    it.copy(
                        recentTransactions = transactions
                    )
                }
            }
        }
    }

}