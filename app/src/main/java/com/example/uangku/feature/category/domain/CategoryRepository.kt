package com.example.uangku.feature.category.domain

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Long): Category?

    suspend fun createCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

}