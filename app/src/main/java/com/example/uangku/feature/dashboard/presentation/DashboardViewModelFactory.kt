package com.example.uangku.feature.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.TransactionUseCase

@RequiresApi(Build.VERSION_CODES.O)
class DashboardViewModelFactory (

    private val periodUseCase: PeriodUseCase,
    private val transactionUseCase: TransactionUseCase,
    private val budgetUseCase: BudgetUseCase

): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(periodUseCase, transactionUseCase, budgetUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}