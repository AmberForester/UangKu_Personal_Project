package com.example.uangku.feature.export_excel.domain

import android.net.Uri
import com.example.uangku.feature.transaction.domain.Transaction
import java.io.File

data class ExportedExcelFile(
    val file: File,
    val uri: Uri? = null
)

interface ExcelExportRepository {
    suspend fun generateExcel(
        transactions: List<Transaction>,
        periodTitle: String
    ): Result<ExportedExcelFile>

    suspend fun saveToUri(
        sourceFile: File,
        destinationUri: Uri
    ): Result<Unit>
}
