package com.example.uangku.feature.export_excel.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class ExportTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
    private val periodUseCase: PeriodUseCase,
    private val excelExportRepository: ExcelExportRepository
) {
    suspend operator fun invoke(option: ExportPeriodOption): Result<ExportedExcelFile> {
        return try {
            val transactions: List<Transaction>
            val periodTitle: String
            val zoneId = ZoneId.systemDefault()

            when (option) {
                ExportPeriodOption.CURRENT_FINANCIAL_PERIOD -> {
                    val period = periodUseCase.getCurrentFinancialPeriod()
                    val startDate = Date.from(period.startDate.atStartOfDay(zoneId).toInstant())
                    val endDate = Date.from(period.endDate.plusDays(1).atStartOfDay(zoneId).toInstant())

                    transactions = transactionRepository.getTransactions(startDate, endDate)
                        .first()
                        .sortedBy { it.date }

                    val dtf = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
                    periodTitle = "${period.startDate.format(dtf)} - ${period.endDate.format(dtf)}"
                }

                ExportPeriodOption.THIS_MONTH -> {
                    val now = LocalDate.now()
                    val startOfMonth = now.withDayOfMonth(1)
                    val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())
                    val startDate = Date.from(startOfMonth.atStartOfDay(zoneId).toInstant())
                    val endDate = Date.from(endOfMonth.plusDays(1).atStartOfDay(zoneId).toInstant())

                    transactions = transactionRepository.getTransactions(startDate, endDate)
                        .first()
                        .sortedBy { it.date }

                    val dtf = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID"))
                    periodTitle = now.format(dtf)
                }

                ExportPeriodOption.ALL_TIME -> {
                    transactions = transactionRepository.getTransactions()
                        .first()
                        .sortedBy { it.date }

                    periodTitle = "Semua Riwayat Transaksi"
                }
            }

            if (transactions.isEmpty()) {
                return Result.failure(IllegalStateException("Tidak ada catatan transaksi pada periode yang dipilih"))
            }

            excelExportRepository.generateExcel(transactions, periodTitle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
