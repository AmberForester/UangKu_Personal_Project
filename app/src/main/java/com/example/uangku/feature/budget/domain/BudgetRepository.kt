package com.example.uangku.feature.budget.domain

import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun getBudgets(): Flow<List<Budget>>

    suspend fun getBudgetById(id: Long): Budget?

    suspend fun getBudgetByCategoryId(categoryId: Long): Budget?

    suspend fun createBudget(budget: Budget)

    suspend fun updateBudget(budget: Budget)

    suspend fun deleteBudget(budget: Budget)

}