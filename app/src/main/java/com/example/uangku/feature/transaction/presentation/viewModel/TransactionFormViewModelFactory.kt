package com.example.uangku.feature.transaction.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.transaction.domain.TransactionUseCase

class TransactionFormViewModelFactory (

    private val categoryUseCase: CategoryUseCase,
    private val transactionUseCase: TransactionUseCase

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(TransactionFormViewModel::class.java)) {
            return TransactionFormViewModel(categoryUseCase, transactionUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}