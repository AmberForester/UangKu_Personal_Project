package com.example.uangku.feature.export_excel.presentation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.export_excel.domain.ExcelExportRepository
import com.example.uangku.feature.export_excel.domain.ExportTransactionsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ExportExcelViewModel(
    private val exportTransactionsUseCase: ExportTransactionsUseCase,
    private val excelExportRepository: ExcelExportRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExportExcelState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ExportExcelEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ExportExcelEvent) {
        when (event) {
            ExportExcelEvent.ShowDialog -> {
                _state.update { it.copy(isDialogVisible = true, errorMessage = null) }
            }
            ExportExcelEvent.DismissDialog -> {
                if (!_state.value.isLoading) {
                    _state.update { it.copy(isDialogVisible = false) }
                }
            }
            is ExportExcelEvent.SelectOption -> {
                _state.update { it.copy(selectedOption = event.option) }
            }
            ExportExcelEvent.ConfirmSave -> {
                exportData(isShare = false)
            }
            ExportExcelEvent.ConfirmShare -> {
                exportData(isShare = true)
            }
            is ExportExcelEvent.OnFileSavedToUri -> {
                saveCachedFileToUri(event.uri)
            }
            ExportExcelEvent.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun exportData(isShare: Boolean) {
        val currentOption = _state.value.selectedOption
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = exportTransactionsUseCase(currentOption)
            result.fold(
                onSuccess = { exportedFile ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isDialogVisible = false,
                            lastGeneratedFile = exportedFile.file
                        )
                    }
                    if (isShare) {
                        val uri = exportedFile.uri
                        if (uri != null) {
                            _effect.emit(ExportExcelEffect.ShareExcel(uri, exportedFile.file.name))
                        }
                    } else {
                        _effect.emit(ExportExcelEffect.LaunchSaveFilePicker(exportedFile.file.name))
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Gagal mengekspor data transaksi"
                        )
                    }
                    _effect.emit(
                        ExportExcelEffect.ShowToast(
                            error.localizedMessage ?: "Gagal mengekspor data transaksi"
                        )
                    )
                }
            )
        }
    }

    private fun saveCachedFileToUri(destinationUri: Uri) {
        val file = _state.value.lastGeneratedFile ?: return
        viewModelScope.launch {
            val result = excelExportRepository.saveToUri(file, destinationUri)
            result.fold(
                onSuccess = {
                    _effect.emit(ExportExcelEffect.ShowToast("Laporan Excel berhasil disimpan di perangkat"))
                },
                onFailure = { error ->
                    _effect.emit(
                        ExportExcelEffect.ShowToast(
                            error.localizedMessage ?: "Gagal menyimpan berkas di perangkat"
                        )
                    )
                }
            )
        }
    }
}
