package com.example.uangku.feature.analysis.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.savings.domain.SavingsUseCase
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class AnalysisUseCase(
    private val transactionRepository: TransactionRepository,
    private val periodUseCase: PeriodUseCase,
    private val savingsUseCase: SavingsUseCase? = null
) {

    suspend fun getFinancialPeriod(month: YearMonth): FinancialPeriod {
        return periodUseCase.getFinancialPeriod(month)
    }

    suspend fun getCurrentPeriodMonth(): YearMonth {
        return periodUseCase.getCurrentPeriodMonth()
    }

    suspend fun getAnalysisOverview(
        month: YearMonth,
        type: Type = Type.EXPENSE
    ): Flow<AnalysisOverview> {
        val currentPeriod = periodUseCase.getFinancialPeriod(month)
        val previousPeriod = periodUseCase.getFinancialPeriod(month.minusMonths(1))

        val zoneId = ZoneId.systemDefault()
        val startDate = Date.from(currentPeriod.startDate.atStartOfDay(zoneId).toInstant())
        val endDate = Date.from(currentPeriod.endDate.plusDays(1).atStartOfDay(zoneId).toInstant())

        val prevStartDate = Date.from(previousPeriod.startDate.atStartOfDay(zoneId).toInstant())
        val prevEndDate = Date.from(previousPeriod.endDate.plusDays(1).atStartOfDay(zoneId).toInstant())

        val daysCount = ChronoUnit.DAYS.between(currentPeriod.startDate, currentPeriod.endDate) + 1

        val savingsFlow: Flow<Double> = savingsUseCase?.getSavedAmountForPeriod(currentPeriod) ?: flowOf(0.0)

        return combine(
            transactionRepository.getTransactions(),
            savingsFlow
        ) { allTransactions, savedAmount ->
            val currentTransactions = allTransactions.filter {
                it.type == type && it.date >= startDate && it.date < endDate
            }

            val totalAmount = currentTransactions.sumOf { it.amount }

            // Hitung total pemasukan periode ini untuk rasio tabungan
            val totalIncome = if (type == Type.INCOME) {
                totalAmount
            } else {
                allTransactions.filter {
                    it.type == Type.INCOME && it.date >= startDate && it.date < endDate
                }.sumOf { it.amount }
            }

            val savingsPercentage = if (totalIncome > 0) {
                ((savedAmount / totalIncome) * 100).toInt()
            } else {
                0
            }

            // 1. Nominal per Kategori (diurutkan terbesar ke terkecil)
            val categorySpendings = currentTransactions
                .groupBy { it.categoryId }
                .map { (catId, txs) ->
                    val catName = txs.firstOrNull()?.categoryName ?: "Lainnya"
                    val amount = txs.sumOf { it.amount }
                    val percentage = if (totalAmount > 0) (amount / totalAmount).toFloat() else 0f
                    CategorySpending(
                        categoryId = catId,
                        categoryName = catName,
                        totalAmount = amount,
                        percentage = percentage,
                        transactionCount = txs.size
                    )
                }
                .sortedByDescending { it.totalAmount }

            // 2. Rata-rata Harian
            val dailyAverage = if (daysCount > 0) totalAmount / daysCount else 0.0

            // 3. Hari dengan Transaksi Tertinggi
            val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val highestDayEntry = currentTransactions
                .groupBy { dayFormat.format(it.date) }
                .maxByOrNull { (_, txs) -> txs.sumOf { it.amount } }

            val highestSpendingDate = highestDayEntry?.value?.firstOrNull()?.date
            val highestSpendingAmount = highestDayEntry?.value?.sumOf { it.amount } ?: 0.0

            // 4. Kategori Terbesar / Terboros
            val topCategory = categorySpendings.firstOrNull()
            val topCategoryName = topCategory?.categoryName
            val topCategoryPercentage = topCategory?.let { (it.percentage * 100).toInt() } ?: 0

            // 5. Perbandingan dengan Bulan Lalu
            val prevTransactions = allTransactions.filter {
                it.type == type && it.date >= prevStartDate && it.date < prevEndDate
            }
            val previousAmount = prevTransactions.sumOf { it.amount }

            val comparison = if (previousAmount > 0) {
                val diff = Math.abs(totalAmount - previousAmount)
                val percentageChange = ((diff / previousAmount) * 100).toInt()
                val isPositive = if (type == Type.EXPENSE) {
                    totalAmount < previousAmount // lebih hemat
                } else {
                    totalAmount > previousAmount // pemasukan lebih tinggi
                }
                SpendingComparison(
                    currentAmount = totalAmount,
                    previousAmount = previousAmount,
                    percentageChange = percentageChange,
                    isPositive = isPositive,
                    hasPreviousData = true
                )
            } else {
                SpendingComparison(
                    currentAmount = totalAmount,
                    previousAmount = 0.0,
                    percentageChange = 0,
                    isPositive = false,
                    hasPreviousData = false
                )
            }

            AnalysisOverview(
                totalAmount = totalAmount,
                type = type,
                insights = SpendingInsights(
                    dailyAverage = dailyAverage,
                    highestSpendingDate = highestSpendingDate,
                    highestSpendingAmount = highestSpendingAmount,
                    topCategoryName = topCategoryName,
                    topCategoryPercentage = topCategoryPercentage,
                    comparison = comparison,
                    savedAmount = savedAmount,
                    savingsPercentage = savingsPercentage
                ),
                categorySpendings = categorySpendings
            )
        }
    }

    suspend fun getCategoryAnalysisDetail(
        categoryId: Int,
        targetMonth: YearMonth,
        type: Type,
        historyMonths: Int = 6
    ): Flow<CategoryAnalysisDetail> {
        val zoneId = ZoneId.systemDefault()

        // 1. Periode Target Bulan Ini
        val targetPeriod = periodUseCase.getFinancialPeriod(targetMonth)
        val targetStartDate = Date.from(targetPeriod.startDate.atStartOfDay(zoneId).toInstant())
        val targetEndDate = Date.from(targetPeriod.endDate.plusDays(1).atStartOfDay(zoneId).toInstant())

        val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale("id", "ID"))

        // Menghitung batas waktu setiap bulan untuk histori tren
        val monthRanges = (historyMonths - 1 downTo 0).map { offset ->
            val m = targetMonth.minusMonths(offset.toLong())
            val p = periodUseCase.getFinancialPeriod(m)
            val s = Date.from(p.startDate.atStartOfDay(zoneId).toInstant())
            val e = Date.from(p.endDate.plusDays(1).atStartOfDay(zoneId).toInstant())
            Triple(m, s, e)
        }

        return transactionRepository.getTransactions().map { allTransactions ->
            val categoryTransactions = allTransactions.filter {
                it.categoryId == categoryId && it.type == type
            }

            val categoryName = categoryTransactions.firstOrNull()?.categoryName ?: "Kategori"

            // Transaksi periode berjalan
            val currentTransactions = categoryTransactions.filter {
                it.date >= targetStartDate && it.date < targetEndDate
            }.sortedByDescending { it.date }

            val currentAmount = currentTransactions.sumOf { it.amount }
            val peakTransaction = currentTransactions.maxByOrNull { it.amount }

            // Riwayat per bulan (6 bulan terakhir)
            val historyPoints = monthRanges.map { (m, s, e) ->
                val mAmount = categoryTransactions
                    .filter { it.date >= s && it.date < e }
                    .sumOf { it.amount }

                MonthlyTrendPoint(
                    month = m,
                    label = m.format(monthFormatter),
                    amount = mAmount
                )
            }

            // Hitung rata-rata bulanan
            val nonZeroMonths = historyPoints.filter { it.amount > 0 }
            val averageMonthlyAmount = if (nonZeroMonths.isNotEmpty()) {
                historyPoints.sumOf { it.amount } / nonZeroMonths.size
            } else 0.0

            val percentageVsAverage = if (averageMonthlyAmount > 0) {
                val diff = Math.abs(currentAmount - averageMonthlyAmount)
                ((diff / averageMonthlyAmount) * 100).toInt()
            } else 0

            val isHigherThanAverage = currentAmount > averageMonthlyAmount

            CategoryAnalysisDetail(
                categoryId = categoryId,
                categoryName = categoryName,
                type = type,
                targetMonth = targetMonth,
                currentMonthAmount = currentAmount,
                transactionCount = currentTransactions.size,
                averageMonthlyAmount = averageMonthlyAmount,
                percentageVsAverage = percentageVsAverage,
                isHigherThanAverage = isHigherThanAverage,
                peakTransaction = peakTransaction,
                monthlyHistory = historyPoints,
                transactions = currentTransactions
            )
        }
    }
}
