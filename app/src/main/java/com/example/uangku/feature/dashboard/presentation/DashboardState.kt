package com.example.uangku.feature.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.budget.domain.BudgetSummary
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionSummary
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class DashboardState (

    val financialPeriod: FinancialPeriod? = null,
    val summary: TransactionSummary = TransactionSummary(),
    val month: YearMonth = YearMonth.now(),
    val budgets: List<BudgetSummary> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()

)