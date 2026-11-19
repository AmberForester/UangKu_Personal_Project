package com.example.uangku.feature.analysis.domain

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.period.domain.PeriodRepository
import com.example.uangku.feature.period.domain.PeriodSettings
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingLog
import com.example.uangku.feature.savings.domain.SavingType
import com.example.uangku.feature.savings.domain.SavingsRepository
import com.example.uangku.feature.savings.domain.SavingsUseCase
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

class AnalysisUseCaseTest {

    private val fakePeriodRepository = object : PeriodRepository {
        override fun getPeriodSettings(): Flow<PeriodSettings> = flowOf(PeriodSettings(startDay = 1))
        override suspend fun updateStartDay(startDay: Int) {}
    }

    private val periodUseCase = PeriodUseCase(fakePeriodRepository)

    private fun createDate(year: Int, month: Int, day: Int): Date {
        val localDate = LocalDate.of(year, month, day)
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

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

    private fun createFakeSavingsUseCase(logs: List<SavingLog>): SavingsUseCase {
        val fakeRepo = object : SavingsRepository {
            override fun getAllGoals(): Flow<List<SavingGoal>> = flowOf(emptyList())
            override fun getGoalById(id: Long): Flow<SavingGoal?> = flowOf(null)
            override suspend fun createGoal(goal: SavingGoal) {}
            override suspend fun updateGoal(goal: SavingGoal) {}
            override suspend fun deleteGoal(id: Long) {}
            override fun getAllLogs(): Flow<List<SavingLog>> = flowOf(logs)
            override fun getLogsByGoalId(goalId: Long): Flow<List<SavingLog>> = flowOf(logs)
            override suspend fun insertLog(log: SavingLog) {}
            override suspend fun deleteLog(id: Long) {}
            override fun getTotalSavings(): Flow<Double> = flowOf(0.0)
            override fun getMostRecentActiveGoal(): Flow<SavingGoal?> = flowOf(null)
        }
        return SavingsUseCase(fakeRepo)
    }

    @Test
    fun getAnalysisOverview_calculates_categories_ranked_descending() = runBlocking {
        // November 2026: 30 days
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            Transaction(
                id = 1L,
                description = "Makan Siang",
                type = Type.EXPENSE,
                amount = 600000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 5)
            ),
            Transaction(
                id = 2L,
                description = "Bensin",
                type = Type.EXPENSE,
                amount = 200000.0,
                categoryId = 2,
                categoryName = "Transport",
                date = createDate(2026, 11, 10)
            ),
            Transaction(
                id = 3L,
                description = "Makan Malam",
                type = Type.EXPENSE,
                amount = 400000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 12)
            ),
            // Income should be excluded from expense analysis
            Transaction(
                id = 4L,
                description = "Gaji",
                type = Type.INCOME,
                amount = 5000000.0,
                categoryId = 3,
                categoryName = "Gaji",
                date = createDate(2026, 11, 1)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val overview = useCase.getAnalysisOverview(currentMonth).first()

        // Total Expense: 600k + 200k + 400k = 1.200.000
        assertEquals(1200000.0, overview.totalExpense, 0.001)

        // Categories: Makanan (1.000.000) then Transport (200.000)
        assertEquals(2, overview.categorySpendings.size)
        assertEquals("Makanan", overview.categorySpendings[0].categoryName)
        assertEquals(1000000.0, overview.categorySpendings[0].totalAmount, 0.001)
        // Makanan percentage: 1000000 / 1200000 = ~0.8333
        assertEquals(0.833f, overview.categorySpendings[0].percentage, 0.01f)

        assertEquals("Transport", overview.categorySpendings[1].categoryName)
        assertEquals(200000.0, overview.categorySpendings[1].totalAmount, 0.001)

        // Top category insight
        assertEquals("Makanan", overview.insights.topCategoryName)
        assertEquals(83, overview.insights.topCategoryPercentage)

        // Daily average (30 days in Nov): 1.200.000 / 30 = 40.000
        assertEquals(40000.0, overview.insights.dailyAverage, 0.001)

        // Highest spending day: 5 Nov (600.000)
        assertEquals(600000.0, overview.insights.highestSpendingAmount, 0.001)
    }

    @Test
    fun getAnalysisOverview_calculates_income_when_type_is_income() = runBlocking {
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            Transaction(
                id = 1L,
                description = "Gaji Kantor",
                type = Type.INCOME,
                amount = 7000000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = createDate(2026, 11, 1)
            ),
            Transaction(
                id = 2L,
                description = "Project Freelance",
                type = Type.INCOME,
                amount = 3000000.0,
                categoryId = 2,
                categoryName = "Freelance",
                date = createDate(2026, 11, 15)
            ),
            Transaction(
                id = 3L,
                description = "Bonus Kantor",
                type = Type.INCOME,
                amount = 1000000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = createDate(2026, 11, 20)
            ),
            // Expense should be excluded from income analysis
            Transaction(
                id = 4L,
                description = "Makan Siang",
                type = Type.EXPENSE,
                amount = 50000.0,
                categoryId = 3,
                categoryName = "Makanan",
                date = createDate(2026, 11, 5)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val overview = useCase.getAnalysisOverview(currentMonth, Type.INCOME).first()

        // Total Income: 7.000.000 + 3.000.000 + 1.000.000 = 11.000.000
        assertEquals(11000000.0, overview.totalAmount, 0.001)
        assertEquals(Type.INCOME, overview.type)

        // Categories: Gaji (8.000.000) then Freelance (3.000.000)
        assertEquals(2, overview.categorySpendings.size)
        assertEquals("Gaji", overview.categorySpendings[0].categoryName)
        assertEquals(8000000.0, overview.categorySpendings[0].totalAmount, 0.001)
        assertEquals(2, overview.categorySpendings[0].transactionCount)

        assertEquals("Freelance", overview.categorySpendings[1].categoryName)
        assertEquals(3000000.0, overview.categorySpendings[1].totalAmount, 0.001)
        assertEquals(1, overview.categorySpendings[1].transactionCount)

        // Top category insight: Gaji
        assertEquals("Gaji", overview.insights.topCategoryName)
        // 8.000.000 / 11.000.000 = ~72%
        assertEquals(72, overview.insights.topCategoryPercentage)

        // Highest income day: 1 Nov (7.000.000)
        assertEquals(7000000.0, overview.insights.highestSpendingAmount, 0.001)
    }

    @Test
    fun getAnalysisOverview_calculates_income_comparison_trend() = runBlocking {
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            // Previous month (Oct 2026): Total 5.000.000
            Transaction(
                id = 1L,
                description = "Gaji Oktober",
                type = Type.INCOME,
                amount = 5000000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = createDate(2026, 10, 1)
            ),
            // Current month (Nov 2026): Total 7.500.000 (Increase by 50%)
            Transaction(
                id = 2L,
                description = "Gaji November",
                type = Type.INCOME,
                amount = 7500000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = createDate(2026, 11, 1)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val overview = useCase.getAnalysisOverview(currentMonth, Type.INCOME).first()
        val comparison = overview.insights.comparison

        assertTrue(comparison != null)
        assertTrue(comparison!!.hasPreviousData)
        assertTrue(comparison.isPositive) // Income grew, so it's positive!
        assertEquals(50, comparison.percentageChange) // (7.500.000 - 5.000.000) / 5.000.000 = 50%
    }

    @Test
    fun getAnalysisOverview_calculates_comparison_with_previous_month() = runBlocking {
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            // Previous month (Oct 2026): Total 2.000.000
            Transaction(
                id = 1L,
                description = "Belanja Oktober",
                type = Type.EXPENSE,
                amount = 2000000.0,
                categoryId = 1,
                categoryName = "Belanja",
                date = createDate(2026, 10, 15)
            ),
            // Current month (Nov 2026): Total 1.500.000 (Cheaper by 25%)
            Transaction(
                id = 2L,
                description = "Belanja November",
                type = Type.EXPENSE,
                amount = 1500000.0,
                categoryId = 1,
                categoryName = "Belanja",
                date = createDate(2026, 11, 15)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val overview = useCase.getAnalysisOverview(currentMonth).first()
        val comparison = overview.insights.comparison

        assertTrue(comparison != null)
        assertTrue(comparison!!.hasPreviousData)
        assertTrue(comparison.isCheaper)
        assertEquals(25, comparison.percentageChange) // (2.000.000 - 1.500.000) / 2.000.000 = 25%
    }

    @Test
    fun getAnalysisOverview_handles_no_previous_month_data() = runBlocking {
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            Transaction(
                id = 1L,
                description = "Belanja November",
                type = Type.EXPENSE,
                amount = 500000.0,
                categoryId = 1,
                categoryName = "Belanja",
                date = createDate(2026, 11, 5)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val overview = useCase.getAnalysisOverview(currentMonth).first()
        val comparison = overview.insights.comparison

        assertTrue(comparison != null)
        assertFalse(comparison!!.hasPreviousData)
    }

    @Test
    fun getCategoryAnalysisDetail_calculates_monthly_history_and_average() = runBlocking {
        val targetMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            // August (3 months ago): 500k
            Transaction(
                id = 1L,
                description = "Makan Siang Agustus",
                type = Type.EXPENSE,
                amount = 500000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 8, 10)
            ),
            // October (last month): 700k
            Transaction(
                id = 2L,
                description = "Makan Siang Oktober",
                type = Type.EXPENSE,
                amount = 700000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 10, 15)
            ),
            // November (current month): 400k and 600k (Total 1.000.000)
            Transaction(
                id = 3L,
                description = "Makan Siang 1 Nov",
                type = Type.EXPENSE,
                amount = 400000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 2)
            ),
            Transaction(
                id = 4L,
                description = "Makan Malam Mewah Nov",
                type = Type.EXPENSE,
                amount = 600000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 20)
            ),
            // Other category should be excluded
            Transaction(
                id = 5L,
                description = "Bensin Nov",
                type = Type.EXPENSE,
                amount = 200000.0,
                categoryId = 2,
                categoryName = "Transport",
                date = createDate(2026, 11, 10)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val detail = useCase.getCategoryAnalysisDetail(
            categoryId = 1,
            targetMonth = targetMonth,
            type = Type.EXPENSE,
            historyMonths = 6
        ).first()

        assertEquals(1, detail.categoryId)
        assertEquals("Makanan", detail.categoryName)
        assertEquals(Type.EXPENSE, detail.type)
        assertEquals(1000000.0, detail.currentMonthAmount, 0.001)
        assertEquals(2, detail.transactionCount)
        assertEquals(2, detail.transactions.size)
        // Transactions sorted by date descending: 20 Nov then 2 Nov
        assertEquals(4L, detail.transactions[0].id)
        assertEquals(3L, detail.transactions[1].id)

        // Peak transaction is 600.000
        assertEquals(4L, detail.peakTransaction?.id)
        assertEquals(600000.0, detail.peakTransaction?.amount ?: 0.0, 0.001)

        // 6 months history: Jun (0), Jul (0), Aug (500k), Sep (0), Oct (700k), Nov (1000k)
        assertEquals(6, detail.monthlyHistory.size)
        assertEquals(0.0, detail.monthlyHistory[0].amount, 0.001) // June
        assertEquals(0.0, detail.monthlyHistory[1].amount, 0.001) // July
        assertEquals(500000.0, detail.monthlyHistory[2].amount, 0.001) // August
        assertEquals(0.0, detail.monthlyHistory[3].amount, 0.001) // September
        assertEquals(700000.0, detail.monthlyHistory[4].amount, 0.001) // October
        assertEquals(1000000.0, detail.monthlyHistory[5].amount, 0.001) // November

        // Average across 3 non-zero months: (500k + 700k + 1000k) / 3 = 733.333,33
        assertEquals(733333.33, detail.averageMonthlyAmount, 1.0)
        assertTrue(detail.isHigherThanAverage) // 1.000.000 > 733.333
    }

    @Test
    fun getCategoryAnalysisDetail_calculates_3_months_history_correctly() = runBlocking {
        val targetMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            // August (4 months ago): 500k -> should be excluded in 3 months range!
            Transaction(
                id = 1L,
                description = "Agustus",
                type = Type.EXPENSE,
                amount = 500000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 8, 10)
            ),
            // September (2 months ago): 300k
            Transaction(
                id = 2L,
                description = "September",
                type = Type.EXPENSE,
                amount = 300000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 9, 10)
            ),
            // October (last month): 700k
            Transaction(
                id = 3L,
                description = "Oktober",
                type = Type.EXPENSE,
                amount = 700000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 10, 15)
            ),
            // November (current month): 800k
            Transaction(
                id = 4L,
                description = "November",
                type = Type.EXPENSE,
                amount = 800000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 2)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val detail = useCase.getCategoryAnalysisDetail(
            categoryId = 1,
            targetMonth = targetMonth,
            type = Type.EXPENSE,
            historyMonths = 3
        ).first()

        assertEquals(3, detail.monthlyHistory.size)
        assertEquals(300000.0, detail.monthlyHistory[0].amount, 0.001) // September
        assertEquals(700000.0, detail.monthlyHistory[1].amount, 0.001) // October
        assertEquals(800000.0, detail.monthlyHistory[2].amount, 0.001) // November

        // Average of 3 months: (300k + 700k + 800k) / 3 = 600.000
        assertEquals(600000.0, detail.averageMonthlyAmount, 0.001)
        assertTrue(detail.isHigherThanAverage) // 800k > 600k
        assertEquals(33, detail.percentageVsAverage) // (800k - 600k) / 600k = 33%
    }

    @Test
    fun getCategoryAnalysisDetail_calculates_12_months_history_correctly() = runBlocking {
        val targetMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            // December 2025 (11 months ago): 1.000.000
            Transaction(
                id = 1L,
                description = "Desember Tahun Lalu",
                type = Type.EXPENSE,
                amount = 1000000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2025, 12, 10)
            ),
            // November 2026 (current month): 500.000
            Transaction(
                id = 2L,
                description = "November Sekarang",
                type = Type.EXPENSE,
                amount = 500000.0,
                categoryId = 1,
                categoryName = "Makanan",
                date = createDate(2026, 11, 5)
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase
        )

        val detail = useCase.getCategoryAnalysisDetail(
            categoryId = 1,
            targetMonth = targetMonth,
            type = Type.EXPENSE,
            historyMonths = 12
        ).first()

        assertEquals(12, detail.monthlyHistory.size)
        // First month is December 2025 (11 months ago)
        assertEquals(YearMonth.of(2025, 12), detail.monthlyHistory[0].month)
        assertEquals(1000000.0, detail.monthlyHistory[0].amount, 0.001)

        // Last month is November 2026
        assertEquals(YearMonth.of(2026, 11), detail.monthlyHistory[11].month)
        assertEquals(500000.0, detail.monthlyHistory[11].amount, 0.001)

        // Average across 2 non-zero months: (1.000.000 + 500.000) / 2 = 750.000
        assertEquals(750000.0, detail.averageMonthlyAmount, 0.001)
        assertFalse(detail.isHigherThanAverage) // 500k < 750k (lebih hemat)
        assertEquals(33, detail.percentageVsAverage) // (750k - 500k) / 750k = 33%
    }

    @Test
    fun getAnalysisOverview_calculates_saved_amount_and_savings_rate() = runBlocking {
        val currentMonth = YearMonth.of(2026, 11)

        val txs = listOf(
            Transaction(
                id = 1L,
                description = "Gaji",
                type = Type.INCOME,
                amount = 10000000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = createDate(2026, 11, 1)
            ),
            Transaction(
                id = 2L,
                description = "Makan",
                type = Type.EXPENSE,
                amount = 2000000.0,
                categoryId = 2,
                categoryName = "Makanan",
                date = createDate(2026, 11, 5)
            )
        )

        // Deposit 2.500.000 to savings in November 2026
        val logs = listOf(
            SavingLog(
                id = 1L,
                goalId = 1L,
                amount = 2500000.0,
                type = SavingType.DEPOSIT,
                date = createDate(2026, 11, 10),
                description = "Setor tabungan"
            )
        )

        val useCase = AnalysisUseCase(
            transactionRepository = createFakeTransactionRepository(txs),
            periodUseCase = periodUseCase,
            savingsUseCase = createFakeSavingsUseCase(logs)
        )

        // 1. Check in INCOME mode: 2.500.000 saved, 25% of 10.000.000 income
        val incomeOverview = useCase.getAnalysisOverview(currentMonth, Type.INCOME).first()
        assertEquals(2500000.0, incomeOverview.insights.savedAmount, 0.001)
        assertEquals(25, incomeOverview.insights.savingsPercentage)

        // 2. Check in EXPENSE mode: savedAmount is 2.500.000
        val expenseOverview = useCase.getAnalysisOverview(currentMonth, Type.EXPENSE).first()
        assertEquals(2500000.0, expenseOverview.insights.savedAmount, 0.001)
    }
}
