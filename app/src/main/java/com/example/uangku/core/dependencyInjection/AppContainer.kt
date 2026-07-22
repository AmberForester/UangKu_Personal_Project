package com.example.uangku.core.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.feature.category.data.CategoryRepoImpl
import com.example.uangku.feature.category.domain.CategoryUseCase

class AppContainer (
    context: Context
) {
    private val database: AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uangku_database"
        ).build()

    private val categoryDao = database.categoryDao()

    private val categoryRepository = CategoryRepoImpl(categoryDao)

    val categoryUseCase = CategoryUseCase(categoryRepository)
}