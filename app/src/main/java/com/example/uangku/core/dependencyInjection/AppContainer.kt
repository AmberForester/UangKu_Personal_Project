package com.example.uangku.core.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.feature.category.data.CategoryRepoImpl
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.transaction.data.TransactionRepoImpl
import com.example.uangku.feature.transaction.domain.TransactionUseCase

class AppContainer (
    context: Context
) {
    private val database: AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uangku_database"
        ).build()

    // category
    private val categoryDao = database.categoryDao()
    private val categoryRepository = CategoryRepoImpl(categoryDao)
    val categoryUseCase = CategoryUseCase(categoryRepository)

    //transaction
    private val transactionDao = database.transcationDao()
    private val transactionRepository = TransactionRepoImpl(transactionDao)
    val transactionUseCase = TransactionUseCase(transactionRepository)
}