package com.example.uangku.feature.category.domain

import com.example.uangku.core.domain.Type
import kotlinx.coroutines.flow.Flow

class CategoryUseCase (
    private val repository: CategoryRepository
) {

    fun getCategories(): Flow<List<Category>> {
        return repository.getCategories()
    }

    suspend fun getCategoryById(id: Long): Category? {
        return repository.getCategoryById(id)
    }

    suspend fun createCategory(category: Category) {
        return repository.createCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        return repository.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        return repository.deleteCategory(category)
    }

}