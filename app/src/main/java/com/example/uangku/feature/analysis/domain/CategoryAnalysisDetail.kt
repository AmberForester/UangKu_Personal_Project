package com.example.uangku.feature.analysis.domain

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.transaction.domain.Transaction
import java.time.YearMonth

data class MonthlyTrendPoint(
    val month: YearMonth,
    val label: String,      // Misal: "Jun", "Jul", "Agt", "Sep", "Okt", "Nov"
    val amount: Double
)

data class CategoryAnalysisDetail(
    val categoryId: Int,
    val categoryName: String,
    val type: Type,
    val targetMonth: YearMonth,
    val currentMonthAmount: Double,
    val transactionCount: Int,
    val averageMonthlyAmount: Double,
    val percentageVsAverage: Int,
    val isHigherThanAverage: Boolean,
    val peakTransaction: Transaction?,
    val monthlyHistory: List<MonthlyTrendPoint>,
    val transactions: List<Transaction>
)
