package com.example.uangku.feature.budget.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel

class BudgetViewModelFactory (

    private val budgetUseCase: BudgetUseCase

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            return BudgetViewModel(budgetUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}