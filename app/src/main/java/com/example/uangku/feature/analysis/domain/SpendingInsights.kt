package com.example.uangku.feature.analysis.domain

import java.util.Date

data class SpendingInsights(
    val dailyAverage: Double = 0.0,
    val highestSpendingDate: Date? = null,
    val highestSpendingAmount: Double = 0.0,
    val topCategoryName: String? = null,
    val topCategoryPercentage: Int = 0,
    val comparison: SpendingComparison? = null,
    val savedAmount: Double = 0.0,
    val savingsPercentage: Int = 0
) {
    val highestDayDate: Date? get() = highestSpendingDate
    val highestDayAmount: Double get() = highestSpendingAmount
}
