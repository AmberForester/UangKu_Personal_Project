package com.example.uangku.feature.transaction.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.domain.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class TransactionUseCase (
    private val repository: TransactionRepository
){

    fun getTransactions(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }

    fun getTransactions(
        month: YearMonth
    ): Flow<List<Transaction>> {

        val zoneId = ZoneId.systemDefault()

        val startDate = Date.from(
            month
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
        )

        val endDate = Date.from(
            month
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
        )

        return repository.getTransactions(
            startDate = startDate,
            endDate = endDate
        )

    }

    fun getMonthlySummary(
        month: YearMonth
    ): Flow<TransactionSummary> {

        return getTransactions(month)
            .map { transactions ->
                val income = transactions
                    .filter { it.type == Type.INCOME }
                    .sumOf { it.amount }

                val expense = transactions
                    .filter { it.type == Type.EXPENSE }
                    .sumOf { it.amount }

                TransactionSummary(
                    income = income,
                    expense = expense,
                    balance = income - expense
                )
            }

    }

    fun getOverallSummary(): Flow<TransactionSummary> {

        return getTransactions()
            .map { transactions ->
                val income = transactions
                    .filter { it.type == Type.INCOME }
                    .sumOf { it.amount }

                val expense = transactions
                    .filter { it.type == Type.EXPENSE }
                    .sumOf { it.amount }

                TransactionSummary(
                    income = income,
                    expense = expense,
                    balance = income - expense
                )
            }

    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return repository.getTransactionById(id)
    }

    suspend fun createTransaction(transaction: Transaction) {
        return repository.createTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        return repository.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        return repository.deleteTransaction(transaction)
    }

}