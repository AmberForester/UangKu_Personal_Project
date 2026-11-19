package com.example.uangku.feature.analysis.domain

data class CategorySpending(
    val categoryId: Int,
    val categoryName: String,
    val totalAmount: Double,
    val percentage: Float, // 0.0f - 1.0f (misal 0.45f = 45%)
    val transactionCount: Int
)
