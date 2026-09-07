package com.example.uangku.feature.transaction.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.period.domain.PeriodUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class TransactionUseCase (

    private val repository: TransactionRepository,
    private val periodUseCase: PeriodUseCase
){

    private fun getTransactions(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }

    suspend fun getTransactions(
        month: YearMonth
    ): Flow<List<Transaction>> {

        val zoneId = ZoneId.systemDefault()

        return periodUseCase.getPeriodSettings().flatMapLatest { settings ->

            val period = periodUseCase.getFinancialPeriod(
                date = month.atDay(1),
                startDay = settings.startDay
            )

            val startDate = Date.from(
                period.startDate
                    .atStartOfDay(zoneId)
                    .toInstant()
            )

            val endDate = Date.from(
                period.endDate
                    .plusDays(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
            )

            repository.getTransactions(
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    fun getFinancialPeriod (
        month: YearMonth
    ): Flow<FinancialPeriod> {
        return periodUseCase
            .getPeriodSettings()
            .map { settings ->
                periodUseCase.getFinancialPeriod(
                    date = month.atDay(1),
                    startDay = settings.startDay
                )
            }
    }

    suspend fun getMonthlySummary(
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