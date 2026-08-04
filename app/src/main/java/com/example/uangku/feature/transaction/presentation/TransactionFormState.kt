package com.example.uangku.feature.transaction.presentation

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.Category
import java.util.Date

data class TransactionFormState (

    val id: Long? = null,
    val categories: List<Category> = emptyList(),
    val filteredCategories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,

    val type: Type = Type.EXPENSE,
    val description: String = "",
    val amount: String = "",
    val date: Date = Date(),

    val isLoading: Boolean = false

){


}