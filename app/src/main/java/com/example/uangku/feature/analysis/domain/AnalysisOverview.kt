package com.example.uangku.feature.analysis.domain

import com.example.uangku.core.domain.Type

data class AnalysisOverview(
    val totalAmount: Double = 0.0,
    val type: Type = Type.EXPENSE,
    val insights: SpendingInsights = SpendingInsights(),
    val categorySpendings: List<CategorySpending> = emptyList()
) {
    val totalExpense: Double get() = totalAmount
}
