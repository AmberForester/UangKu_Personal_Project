package com.example.uangku.feature.export_excel.domain

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.period.domain.PeriodRepository
import com.example.uangku.feature.period.domain.PeriodSettings
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.Date

class ExportTransactionsUseCaseTest {

    private val fakePeriodRepository = object : PeriodRepository {
        override fun getPeriodSettings(): Flow<PeriodSettings> = flowOf(PeriodSettings(startDay = 1))
        override suspend fun updateStartDay(startDay: Int) {}
    }

    private val periodUseCase = PeriodUseCase(fakePeriodRepository)

    private fun createFakeTransactionRepository(transactions: List<Transaction>): TransactionRepository {
        return object : TransactionRepository {
            override fun getTransactions(): Flow<List<Transaction>> = flowOf(transactions)
            override fun getTransactions(startDate: Date, endDate: Date): Flow<List<Transaction>> = flowOf(transactions)
            override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> = flowOf(transactions)
            override suspend fun getTransactionById(id: Long): Transaction? = transactions.find { it.id == id }
            override suspend fun createTransaction(transaction: Transaction) {}
            override suspend fun updateTransaction(transaction: Transaction) {}
            override suspend fun deleteTransaction(transaction: Transaction) {}
            override suspend fun insertTransactions(transactions: List<Transaction>) {}
            override suspend fun deleteAllTransactions() {}
        }
    }

    private val fakeExcelExportRepository = object : ExcelExportRepository {
        var lastExportedTransactions: List<Transaction>? = null
        var lastExportedTitle: String? = null

        override suspend fun generateExcel(
            transactions: List<Transaction>,
            periodTitle: String
        ): Result<ExportedExcelFile> {
            lastExportedTransactions = transactions
            lastExportedTitle = periodTitle
            val dummyFile = File("dummy.xlsx")
            return Result.success(ExportedExcelFile(dummyFile))
        }

        override suspend fun saveToUri(
            sourceFile: File,
            destinationUri: android.net.Uri
        ): Result<Unit> {
            return Result.success(Unit)
        }
    }

    @Test
    fun `invoke with empty transactions returns failure`() = runBlocking {
        val emptyRepo = createFakeTransactionRepository(emptyList())
        val useCase = ExportTransactionsUseCase(emptyRepo, periodUseCase, fakeExcelExportRepository)

        val result = useCase(ExportPeriodOption.ALL_TIME)

        assertTrue(result.isFailure)
        assertEquals("Tidak ada catatan transaksi pada periode yang dipilih", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with transactions exports successfully sorted by date`() = runBlocking {
        val date1 = Date(1000)
        val date2 = Date(2000)
        val tx1 = Transaction(id = 1, description = "T1", type = Type.EXPENSE, amount = 1000.0, categoryId = 1, categoryName = "A", date = date2)
        val tx2 = Transaction(id = 2, description = "T2", type = Type.INCOME, amount = 2000.0, categoryId = 2, categoryName = "B", date = date1)

        val repo = createFakeTransactionRepository(listOf(tx1, tx2))
        val useCase = ExportTransactionsUseCase(repo, periodUseCase, fakeExcelExportRepository)

        val result = useCase(ExportPeriodOption.ALL_TIME)

        if (result.isFailure) {
            result.exceptionOrNull()?.printStackTrace()
        }
        assertTrue(result.isSuccess)
        val exported = fakeExcelExportRepository.lastExportedTransactions
        assertEquals(2, exported?.size)
        // Check sorting: date1 (earlier) should come before date2 (later)
        assertEquals(2L, exported?.get(0)?.id)
        assertEquals(1L, exported?.get(1)?.id)
    }
}
