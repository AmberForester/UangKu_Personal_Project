package com.example.uangku.core.dependencyInjection

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.core.database.DatabaseCallback
import com.example.uangku.feature.budget.data.BudgetRepoImpl
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.category.data.CategoryRepoImpl
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.transaction.data.TransactionRepoImpl
import com.example.uangku.feature.transaction.domain.TransactionUseCase

@RequiresApi(Build.VERSION_CODES.O)
class AppContainer (
    context: Context
) {
    private val database: AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uangku_database"
        )
        .addCallback(DatabaseCallback())
        .build()

    // category
    private val categoryDao = database.categoryDao()
    private val categoryRepository = CategoryRepoImpl(categoryDao)
    val categoryUseCase = CategoryUseCase(categoryRepository)

    //transaction
    private val transactionDao = database.transactionDao()
    private val transactionRepository = TransactionRepoImpl(transactionDao)
    val transactionUseCase = TransactionUseCase(transactionRepository)

    //budget
    private val budgetDao = database.budgetDao()
    private val budgetRepository = BudgetRepoImpl(budgetDao)
    val budgetUseCase = BudgetUseCase(budgetRepository, transactionRepository, categoryRepository)
}