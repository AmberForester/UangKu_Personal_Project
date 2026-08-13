package com.example.uangku.feature.budget.data

import com.example.uangku.feature.budget.domain.Budget
import com.example.uangku.feature.budget.domain.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepoImpl (
    private val budgetDao: BudgetDao
) : BudgetRepository{
    override fun getBudgets(): Flow<List<Budget>> {
        return budgetDao.getBudgets().map { budgets ->
            budgets.map { it.toDomain() }
        }
    }

    override suspend fun getBudgetById(id: Long): Budget? {
        return budgetDao.getBudgetById(id)?.toDomain()
    }

    override suspend fun getBudgetByCategoryId(categoryId: Long): Budget? {
        return budgetDao.getBudgetByCategoryId(categoryId)?.toDomain()
    }

    override suspend fun createBudget(budget: Budget) {
        return budgetDao.createBudget(budget.toEntity())
    }

    override suspend fun updateBudget(budget: Budget) {
        return budgetDao.updateBudget(budget.toEntity())
    }

    override suspend fun deleteBudget(budget: Budget) {
        return budgetDao.deleteBudget(budget.toEntity())
    }

}