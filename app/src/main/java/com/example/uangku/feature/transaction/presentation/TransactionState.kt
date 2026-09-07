package com.example.uangku.feature.transaction.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionSummary
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class TransactionState (

    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedTransaction: Transaction? = null,

    val selectedMonth: YearMonth = YearMonth.now(),
    val financialPeriod: FinancialPeriod? = null,

    val monthlySummary: TransactionSummary = TransactionSummary(),
    val overallSummary: TransactionSummary = TransactionSummary(),

    )