package com.example.uangku.feature.dashboard.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.savings.domain.SavingsUseCase
import com.example.uangku.feature.transaction.domain.TransactionSummary
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(

    private val periodUseCase: PeriodUseCase,
    private val transactionUseCase: TransactionUseCase,
    private val budgetUseCase: BudgetUseCase,
    private val savingsUseCase: SavingsUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())

    val state = _state.asStateFlow()

    init {
        observePeriod()
        observeMonthlySummary()
        observeCurrentPeriodSavings()
    }

    fun onEvent(event: DashboardEvent){

        when(event){

            DashboardEvent.onScreenOpen -> {
                loadFinancialPeriod()
                loadBudgetSummaries()
                loadRecentTransactions()
                loadSavings()
                Log.d("this month", _state.value.month.toString())
            }
        }
    }

    private fun observePeriod() {
        viewModelScope.launch {
            periodUseCase.observeCurrentFinancialPeriod().collect { period ->
                val currentMonth = periodUseCase.getCurrentPeriodMonth()
                _state.update {
                    it.copy(
                        financialPeriod = period,
                        month = currentMonth
                    )
                }
            }
        }
    }

    private fun observeMonthlySummary() {
        viewModelScope.launch {
            state.map { it.financialPeriod }
                .distinctUntilChanged()
                .flatMapLatest { period ->
                    if (period != null) {
                        transactionUseCase.getMonthlySummary(period)
                    } else {
                        flowOf(TransactionSummary())
                    }
                }
                .collect { summary ->
                    _state.update {
                        it.copy(summary = summary)
                    }
                }
        }
    }

    private fun observeCurrentPeriodSavings() {
        viewModelScope.launch {
            state.map { it.financialPeriod }
                .distinctUntilChanged()
                .flatMapLatest { period ->
                    if (period != null) {
                        savingsUseCase.getSavedAmountForPeriod(period)
                    } else {
                        flowOf(0.0)
                    }
                }
                .collect { periodSaved ->
                    _state.update {
                        it.copy(currentPeriodSavings = periodSaved)
                    }
                }
        }
    }

    private fun loadFinancialPeriod() {
        viewModelScope.launch {
            val period = periodUseCase.getCurrentFinancialPeriod()
            val currentMonth = periodUseCase.getCurrentPeriodMonth()

            _state.update {
                it.copy(
                    financialPeriod = period,
                    month = currentMonth
                )
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

    private fun loadSavings() {
        viewModelScope.launch {
            savingsUseCase.getMostRecentActiveGoal().collect { goal ->
                _state.update {
                    it.copy(mostRecentSavingGoal = goal)
                }
            }
        }
        viewModelScope.launch {
            savingsUseCase.getTotalLockedSavings().collect { total ->
                _state.update {
                    it.copy(totalSavings = total)
                }
            }
        }
    }

}
