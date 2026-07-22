package com.example.uangku.feature.category.presentation

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type

data class CategoryState (

    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: Category? = null,
    val name: String = "",
    val icon: String = "",
    val type: Type = Type.EXPENSE,
    val showFormDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,

    )