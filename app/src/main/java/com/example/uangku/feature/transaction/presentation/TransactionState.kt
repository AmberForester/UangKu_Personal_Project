package com.example.uangku.feature.transaction.presentation

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.transaction.domain.Transaction
import java.time.LocalDate
import java.util.Date

data class TransactionState (

    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedTransaction: Transaction? = null,

)