package com.example.uangku.feature.export_excel.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.uangku.feature.export_excel.domain.ExcelExportRepository
import com.example.uangku.feature.export_excel.domain.ExportedExcelFile
import com.example.uangku.feature.transaction.domain.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExcelExportRepoImpl(
    private val context: Context,
    private val excelGenerator: ExcelGenerator = ExcelGenerator()
) : ExcelExportRepository {

    override suspend fun generateExcel(
        transactions: List<Transaction>,
        periodTitle: String
    ): Result<ExportedExcelFile> = withContext(Dispatchers.IO) {
        try {
            val exportDir = File(context.cacheDir, "exports")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(exportDir, "UangKu_Transaksi_$timestamp.xlsx")

            excelGenerator.generate(
                outputFile = file,
                transactions = transactions,
                periodTitle = periodTitle
            )

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            Result.success(ExportedExcelFile(file = file, uri = uri))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveToUri(
        sourceFile: File,
        destinationUri: Uri
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@withContext Result.failure(IllegalStateException("Tidak dapat membuka berkas tujuan untuk menulis"))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
