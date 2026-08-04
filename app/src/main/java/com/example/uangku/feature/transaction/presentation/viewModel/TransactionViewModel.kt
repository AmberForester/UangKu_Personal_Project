package com.example.uangku.feature.transaction.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import com.example.uangku.feature.transaction.presentation.TransactionEvent
import com.example.uangku.feature.transaction.presentation.TransactionFormState
import com.example.uangku.feature.transaction.presentation.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel (

    private val transactionUseCase: TransactionUseCase,

) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    fun onEvent(event: TransactionEvent) {

        when (event) {

            TransactionEvent.onScreenOpen -> {
                loadTransactions()
            }

            TransactionEvent.onAddClick -> {

            }

            is TransactionEvent.onEditClick -> {
            }

            is TransactionEvent.onDeleteClick -> {
            }

            TransactionEvent.onDeleteConfirm -> {
            }

            is TransactionEvent.onTypeChange -> {

            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionUseCase.getTransactions().collect(){ transactions ->
                _state.update {
                    it.copy(
                        transactions = transactions
                    )
                }
            }
        }
    }
}


