package com.example.uangku.feature.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.category.domain.CategoryUseCase

class CategoryViewModelFactory (
    private val categoryUseCase: CategoryUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}