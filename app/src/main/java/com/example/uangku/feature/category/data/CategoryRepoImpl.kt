package com.example.uangku.feature.category.data

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.feature.category.domain.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepoImpl(

    private val categoryDao: CategoryDao

) : CategoryRepository {

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories()
            .map { categories ->
                categories.map { it.toDomain() }
            }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoriesById(id)?.toDomain()
    }

    override suspend fun createCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    // backup and restore
    override suspend fun insertCategories(categories: List<Category>) {
        categoryDao.insertCategories(
            categories.map { it.toEntity() }
        )
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }
}