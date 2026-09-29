package com.example.uangku.feature.transaction.presentation.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.TransactionUseCase

class TransactionViewModelFactory (

    private val transactionUseCase: TransactionUseCase,
    private val periodUseCase: PeriodUseCase

) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionUseCase, periodUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}