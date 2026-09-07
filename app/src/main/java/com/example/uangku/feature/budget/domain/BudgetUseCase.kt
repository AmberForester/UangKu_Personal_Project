package com.example.uangku.feature.budget.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.CategoryRepository
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class BudgetUseCase (

    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val periodUseCase: PeriodUseCase

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

    suspend fun getBudgetSummaries(): Flow<List<BudgetSummary>>{

        val period = periodUseCase.getFinancialPeriod()

        val startDate = Date.from(
            period.startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val endDate = Date.from(
            period.endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        return combine(

            budgetRepository.getBudgets(),
            transactionRepository.getTransactions(),
            categoryRepository.getCategories(),

        ) {
            budgets, transactions, categories ->

            val expenseCategory = categories
                .filter { category ->
                    category.type == Type.EXPENSE
                }.mapNotNull { category ->
                    category.id
                }.toSet()

            val spentByCategory = transactions
                .filter { transaction ->
                transaction.type == Type.EXPENSE &&
                    transaction.date >= startDate &&
                        transaction.date < endDate
            }
                .groupBy { it.categoryId }
                .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

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

    suspend fun getBudgetOverview(): Flow<BudgetOverview> {

        val period = periodUseCase.getFinancialPeriod()

        val startDate = Date.from(
            period.startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val endDate = Date.from(
            period.endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        return combine(

            budgetRepository.getBudgets(),
            transactionRepository.getTransactions(),

        ) { budgets, transactions ->

            val income = transactions
                .filter { it.type == Type.INCOME &&
                        it.date >= startDate &&
                        it.date < endDate
                }
                .sumOf { it.amount }

            val allocated = budgets
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

        val period = periodUseCase.getFinancialPeriod()

        val startDate = Date.from(
            period.startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val endDate = Date.from(
            period.endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        val transactions = transactionRepository.getTransactions().first()

        val budgets = budgetRepository.getBudgets().first()

        val totalIncome = transactions
            .filter {
                it.type == Type.INCOME &&
                        it.date >= startDate &&
                        it.date < endDate
            }
            .sumOf { it.amount }

        val totalAllocated = budgets
            .filter { it.id != budgetId }
            .sumOf {
                it.amount ?: 0.0
            }

        return (totalIncome - totalAllocated)
            .coerceAtLeast(0.0)
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//private fun Date.toLocalDate(): LocalDate {
//
//    return toInstant()
//        .atZone(ZoneId.systemDefault())
//        .toLocalDate()
//}