package com.example.uangku.feature.export_excel.presentation

import android.net.Uri
import com.example.uangku.feature.export_excel.domain.ExportPeriodOption

sealed interface ExportExcelEvent {
    data object ShowDialog : ExportExcelEvent
    data object DismissDialog : ExportExcelEvent
    data class SelectOption(val option: ExportPeriodOption) : ExportExcelEvent
    data object ConfirmSave : ExportExcelEvent
    data object ConfirmShare : ExportExcelEvent
    data class OnFileSavedToUri(val uri: Uri) : ExportExcelEvent
    data object ClearError : ExportExcelEvent
}
