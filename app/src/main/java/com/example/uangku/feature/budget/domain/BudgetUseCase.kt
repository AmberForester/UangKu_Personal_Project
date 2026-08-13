package com.example.uangku.feature.budget.domain

import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.isSameMonth
import com.example.uangku.feature.category.domain.CategoryRepository
import com.example.uangku.feature.transaction.domain.TransactionRepository
import com.example.uangku.feature.transaction.domain.TransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

class BudgetUseCase (

    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository

){

    fun getBudgets(): Flow<List<Budget>> {
        return budgetRepository.getBudgets()
    }

    suspend fun getBudgetById(id: Long): Budget? {
        return budgetRepository.getBudgetById(id)
    }

    suspend fun saveBudget(budget: Budget){

        val available = getAvailableAmount( budgetId = budget.id)

        if((budget.amount ?: 0.0) > available){
            throw IllegalArgumentException(
                "Budget melebihi dana yang tersedia"
            )
        }

        if(budget.id == null){
            budgetRepository.createBudget(budget)
        } else {
            budgetRepository.updateBudget(budget)
        }

    }

    suspend fun deleteBudget(budget: Budget) {
        return budgetRepository.deleteBudget(budget)
    }

    fun getBudgetSummaries(): Flow<List<BudgetSummary>>{

        val today = Date()

        return combine(

            budgetRepository.getBudgets(),
            transactionRepository.getTransactions(),
            categoryRepository.getCategories()


        ) {
            budgets, transactions, categories ->

            val expenseCategory = categories.filter { category -> category.type == Type.EXPENSE }.mapNotNull { category -> category.id }.toSet()

            val spentByCategory = transactions.filter { it.type == Type.EXPENSE && it.date.isSameMonth(today) }
                .groupBy { it.categoryId }
                .mapValues { (_, transactions) -> transactions.sumOf { it.amount }
                }

            budgets
                .filter { it.categoryId.toLong() in expenseCategory }
                .map { budget ->

                val spent = spentByCategory[budget.categoryId] ?: 0.0

                val budgetAmount = budget.amount ?: 0.0

                BudgetSummary(
                    budgetId = budget.id,
                    categoryId = budget.categoryId,
                    categoryName = budget.categoryName,
                    budgetAmount = budgetAmount,
                    spent = spent,
                )
            }
        }
    }

    fun getBudgetOverview(): Flow<BudgetOverview> {

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        return combine(

            budgetRepository.getBudgets(),
            transactionRepository.getTransactions()

        ) { budgets, transactions ->

            val income = transactions
                .filter { it.type == Type.INCOME && it.date.isSameMonth(Date()) }
                .sumOf { it.amount }

            val allocated = budgets
                .filter { it.month == currentMonth && it.year == currentYear }
                .sumOf { it.amount ?: 0.0 }

            val available = (income - allocated).coerceAtLeast(0.0)

            BudgetOverview(
                income = income,
                allocated = allocated,
                available = available
            )
        }
    }

    suspend fun getAvailableAmount(
        budgetId: Long? = null
    ): Double {

        val calendar = Calendar.getInstance()

        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        val transactions = transactionRepository
            .getTransactions()
            .first()

        val budgets = budgetRepository
            .getBudgets()
            .first()

        val totalIncome = transactions
            .filter {
                it.type == Type.INCOME &&
                        it.date.isSameMonth(Date())
            }
            .sumOf { it.amount }

        val totalAllocated = budgets
            .filter {
                it.month == currentMonth &&
                        it.year == currentYear &&
                        it.id != budgetId
            }
            .sumOf {
                it.amount ?: 0.0
            }

        return (totalIncome - totalAllocated)
            .coerceAtLeast(0.0)
    }
}