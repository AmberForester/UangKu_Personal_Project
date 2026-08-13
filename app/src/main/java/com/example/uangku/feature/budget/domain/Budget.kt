package com.example.uangku.feature.budget.domain

data class Budget(
    val id: Long? = null,
    val amount: Double?,

    val categoryId: Int,
    val categoryName: String,

    val month: Int?,
    val year: Int?
)