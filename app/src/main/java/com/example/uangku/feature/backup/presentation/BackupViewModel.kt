package com.example.uangku.feature.backup.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.backup.data.FileManager
import com.example.uangku.feature.backup.data.JsonSerializer
import com.example.uangku.feature.backup.domain.BackupUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupViewModel (

    private val backupUseCase: BackupUseCase,
    private val jsonSerializer: JsonSerializer,
    private val fileManager: FileManager

) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BackupEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: BackupEvent){
        when(event) {

            BackupEvent.PrepareExport -> {
                prepareExport()
            }

            is BackupEvent.ExportFile -> {
                Log.d("BACK UP JSON: ", "viewModel received")

                exportBackup(event.uri)
            }

            BackupEvent.PrepareImport -> {
                requestImport()
            }

            is BackupEvent.ImportFile -> {
                importBackup(event.uri)
            }

            BackupEvent.Restore -> {
                restoreData()
            }

            BackupEvent.CancelRestore -> {
                _state.update {
                    it.copy(
                        bufferRestore = null
                    )
                }
            }

            BackupEvent.ClearMessage -> {
                _state.update {
                    it.copy(
                        message = null
                    )
                }
            }

        }
    }

    private fun prepareExport() {
        viewModelScope.launch {

            try {

                _state.update {
                    it.copy(
                        message = null,
                    )
                }

                val backupData = backupUseCase.getBackupData()

                val json = jsonSerializer.toJson(backupData)

                _state.update {
                    it.copy(
                        exportJson = json
                    )
                }
                _effect.emit(BackupEffect.LaunchExportFilePicker)

            } catch (e: Exception){
                _state.update {
                    it.copy(
                        message = "Gagal mengambil data"
                    )
                }
            }
        }
    }

    private fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            try {
                val json = _state.value.exportJson ?: return@launch

                fileManager.write(uri = uri, content = json)

                _state.update {
                    it.copy(
                        exportJson = null,
                        message = "Ekspor Berhasil!"
                    )
                }

            } catch (e: Exception){
                _state.update {
                    it.copy(
                        exportJson = null,
                        message = "Ekspor Gagal!"
                    )
                }
            }
        }
    }

    private fun requestImport() {
        viewModelScope.launch {
            _effect.emit(
                BackupEffect.LaunchImportFilePicker
            )
        }
    }

    private fun importBackup(uri: Uri) {
        viewModelScope.launch {
            try {
                val json = fileManager.read(uri)

                val data = jsonSerializer.fromJson(json)

                Log.d(
                    "IMPORT JSON",
                    "json parsed successfully: $json"
                )

                _state.update {
                    it.copy(
                        bufferRestore = data
                    )
                }

            } catch (e: Exception){
                _state.update {
                    it.copy(
                        exportJson = null,
                        message = "Import Gagal!"
                    )
                }
            }
        }
    }

    private fun restoreData() {
        viewModelScope.launch {
            val data = _state.value.bufferRestore ?: return@launch

            try {

                backupUseCase.restoreData(data)

                _state.update {
                    it.copy(
                        message = "Data berhasil dipulihkan!",
                        bufferRestore = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        message = "Data gagal dipulihkan!",
                        bufferRestore = null
                    )
                }
            }
        }
    }
}