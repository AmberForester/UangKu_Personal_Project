package com.example.uangku.feature.export_excel.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.export_excel.domain.ExcelExportRepository
import com.example.uangku.feature.export_excel.domain.ExportTransactionsUseCase

@RequiresApi(Build.VERSION_CODES.O)
class ExportExcelViewModelFactory(
    private val exportTransactionsUseCase: ExportTransactionsUseCase,
    private val excelExportRepository: ExcelExportRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExportExcelViewModel::class.java)) {
            return ExportExcelViewModel(exportTransactionsUseCase, excelExportRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
