package com.example.uangku.feature.export_excel.presentation

import com.example.uangku.feature.export_excel.domain.ExportPeriodOption
import java.io.File

data class ExportExcelState(
    val isDialogVisible: Boolean = false,
    val selectedOption: ExportPeriodOption = ExportPeriodOption.CURRENT_FINANCIAL_PERIOD,
    val isLoading: Boolean = false,
    val lastGeneratedFile: File? = null,
    val errorMessage: String? = null
)
