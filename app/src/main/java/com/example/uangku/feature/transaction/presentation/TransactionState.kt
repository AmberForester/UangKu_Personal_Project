package com.example.uangku.feature.transaction.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.category.domain.Category
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

    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val isFilterSheetVisible: Boolean = false,
    val categories: List<Category> = emptyList()

) {
    val filteredTransactions: List<Transaction>
        get() = transactions.filter { transaction ->
            val matchesSearch = searchQuery.isBlank() ||
                    transaction.description.contains(searchQuery.trim(), ignoreCase = true) ||
                    transaction.categoryName.contains(searchQuery.trim(), ignoreCase = true)
            val matchesCategory = selectedCategory == null ||
                    transaction.categoryId.toLong() == (selectedCategory.id ?: -1L)
            matchesSearch && matchesCategory
        }

    val hasActiveFilter: Boolean
        get() = searchQuery.isNotBlank() || selectedCategory != null
}