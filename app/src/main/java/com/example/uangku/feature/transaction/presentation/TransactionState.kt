package com.example.uangku.feature.transaction.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionSummary
import java.time.LocalDate
import java.time.YearMonth
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
data class TransactionState (

    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedTransaction: Transaction? = null,

    val selectedMonth: YearMonth = YearMonth.now(),

    val monthlySummary: TransactionSummary = TransactionSummary(),
    val overallSummary: TransactionSummary = TransactionSummary(),

    )