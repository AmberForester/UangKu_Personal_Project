package com.example.uangku.feature.export_excel.presentation

import android.net.Uri

sealed interface ExportExcelEffect {
    data class LaunchSaveFilePicker(val defaultFileName: String) : ExportExcelEffect
    data class ShareExcel(val uri: Uri, val fileName: String) : ExportExcelEffect
    data class ShowToast(val message: String) : ExportExcelEffect
}
