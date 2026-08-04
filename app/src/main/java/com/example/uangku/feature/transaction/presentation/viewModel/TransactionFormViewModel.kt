package com.example.uangku.feature.transaction.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import com.example.uangku.feature.transaction.presentation.TransactionFormEvent
import com.example.uangku.feature.transaction.presentation.TransactionFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionFormViewModel (

    private val categoryUseCase: CategoryUseCase,
    private val transactionUseCase: TransactionUseCase

) : ViewModel() {

    private val _formState = MutableStateFlow(TransactionFormState())
    val formState = _formState.asStateFlow()

    fun onEvent(event: TransactionFormEvent){
        when(event){

            is TransactionFormEvent.OnAmountChange -> {
                _formState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
            }

            is TransactionFormEvent.OnCategoryChange -> {
                _formState.update {
                    it.copy(
                        selectedCategory = event.category
                    )
                }
            }

            is TransactionFormEvent.OnDateChange -> {
                _formState.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is TransactionFormEvent.OnDescriptionChange -> {
                _formState.update {
                    it.copy(
                        description = event.desc
                    )
                }
            }

            is TransactionFormEvent.OnTypeChange -> {
                _formState.update {
                    it.copy(
                        type = event.type
                    )
                }
                filterCategories(event.type)
            }

            is TransactionFormEvent.OnScreenOpen -> {

                resetForm()
                loadCategories()
                filterCategories(Type.EXPENSE)

                if(event.id != -1L){
                    loadTransaction(event.id)
                }
            }

            TransactionFormEvent.OnSave -> {
                if(_formState.value.id == null){
                    createTransaction()
                } else {
                    Log.d("edit transaction", "id terbaca, id = ${_formState.value.id}")
                    editTransaction()
                }
            }

        }
    }

    private fun loadTransaction(id: Long) {
        viewModelScope.launch {
            val transaction = transactionUseCase.getTransactionById(id) ?: return@launch
            _formState.update {
                it.copy(
                    id = transaction.id,
                    description = transaction.description,
                    amount = transaction.amount.toInt().toString(),
                    date = transaction.date,
                    type = transaction.type
                )
            }
            filterCategories(transaction.type)

            _formState.update {
                it.copy(
                    selectedCategory = it.filteredCategories.firstOrNull() { category ->
                        (category.id?.toInt() ?: 0) == transaction.categoryId
                    }
                )
            }
        }
    }

    private fun loadCategories(){
        viewModelScope.launch {
            categoryUseCase.getCategories().collect { categories ->
                _formState.update {
                    it.copy(
                        categories = categories
                    )
                }
                filterCategories(_formState.value.type)
            }
        }
    }

    private fun filterCategories(type: Type){

        val filtered = _formState.value.categories.filter {
            it.type == type
        }

        _formState.update {
            it.copy(
                filteredCategories = filtered,
                selectedCategory = filtered.firstOrNull()
            )
        }
    }

    private fun createTransaction(){
        viewModelScope.launch {
            val transaction = Transaction(
                date = _formState.value.date,
                description = _formState.value.description,
                type = _formState.value.type,
                amount = _formState.value.amount.toDouble(),
                categoryId = _formState.value.selectedCategory!!.id!!.toInt(),
                categoryName = _formState.value.selectedCategory!!.name
            )
            transactionUseCase.createTransaction(transaction)
        }
    }

    private fun editTransaction() {
        viewModelScope.launch {
            val transaction = Transaction(
                id = _formState.value.id,
                date = _formState.value.date,
                description = _formState.value.description,
                type = _formState.value.type,
                amount = _formState.value.amount.toDouble(),
                categoryId = _formState.value.selectedCategory!!.id!!.toInt(),
                categoryName = _formState.value.selectedCategory!!.name
            )
            transactionUseCase.updateTransaction(transaction)
        }
    }
    private fun resetForm() {
        _formState.value = TransactionFormState()
    }
}