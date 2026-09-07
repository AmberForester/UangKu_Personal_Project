package com.example.uangku.feature.transaction.presentation.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import com.example.uangku.feature.transaction.presentation.TransactionEvent
import com.example.uangku.feature.transaction.presentation.TransactionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModel (

    private val transactionUseCase: TransactionUseCase,

) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    init {
        observeTransactions()
        observeMonthlySummary()
        observeFinancialPeriod()
    }

    fun onEvent(event: TransactionEvent) {

        when (event) {

            TransactionEvent.onScreenOpen -> {
                loadOverallSummary()
            }

            TransactionEvent.onAddClick -> {}

            TransactionEvent.onNextMonth -> {
                nextMonth()
            }

            TransactionEvent.onPreviousMonth -> {
                previousMonth()
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {

            val state = state.value

            transactionUseCase.getTransactions(state.selectedMonth).collect { transactions ->
                _state.update {
                    it.copy(
                        transactions = transactions
                    )
                }
            }
        }
    }

    private fun observeTransactions(){
        viewModelScope.launch {
            state
                .map { it.selectedMonth }
                .distinctUntilChanged()
                .flatMapLatest { month ->
                    transactionUseCase.getTransactions(month)
                }
                .collect { transactions ->

                    _state.update {
                        it.copy(
                            transactions = transactions
                        )
                    }

                }
        }
    }

    private fun observeMonthlySummary(){
        viewModelScope.launch {
            state.map { it.selectedMonth }
                .distinctUntilChanged()
                .flatMapLatest { month ->
                    transactionUseCase.getMonthlySummary(month)
                }
                .collect { summary ->
                    _state.update {
                        it.copy(
                            monthlySummary = summary
                        )
                    }
                }
        }
    }

    private fun observeFinancialPeriod() {
        viewModelScope.launch {
            state.map { it.selectedMonth }
                .distinctUntilChanged()
                .flatMapLatest { month ->
                    transactionUseCase.getFinancialPeriod(month)
                }
                .collect { financialPeriod ->
                    _state.update {
                        it.copy(
                            financialPeriod = financialPeriod
                        )
                    }
                }
        }
    }

    private fun loadOverallSummary(){
        viewModelScope.launch {
            transactionUseCase.getOverallSummary().collect { summary ->
                _state.update {
                    it.copy(
                        overallSummary = summary
                    )
                }
            }
        }
    }

    private fun nextMonth() {
        _state.update {
            it.copy(
                selectedMonth = it.selectedMonth.plusMonths(1)
            )
        }
    }

    private fun previousMonth() {
        _state.update {
            it.copy(
                selectedMonth = it.selectedMonth.minusMonths(1)
            )
        }
    }
}


