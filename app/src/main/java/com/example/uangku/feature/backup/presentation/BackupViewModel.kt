package com.example.uangku.feature.backup.presentation

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettingsUseCase
import com.example.uangku.feature.backup.domain.manual.BackupUseCase
import com.example.uangku.feature.google_drive.domain.GoogleDriveUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class BackupViewModel (

    private val backupUseCase: BackupUseCase,
    private val googleDriveUseCase: GoogleDriveUseCase,
    private val autoBackupSettingsUseCase: AutoBackupSettingsUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BackupEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeAutoBackupSettings()
        checkGoogleDriveAuthorization()
    }

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

            BackupEvent.ConnectGoogleDrive -> {
                requestGoogleDriveAuthorization()
            }

            BackupEvent.DisconnectGoogleDrive -> {
                disconnectGoogleDrive()
            }

            is BackupEvent.EnableAutoBackup -> {
                viewModelScope.launch {
                    autoBackupSettingsUseCase.setEnabled(event.enable)
                }
            }

            is BackupEvent.SetAutoBackupFrequency -> {
                viewModelScope.launch {
                    autoBackupSettingsUseCase.setFrequency(event.frequency)
                }
            }

            BackupEvent.CheckAuth -> {
                checkGoogleDriveAuthorization()
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

                val json = backupUseCase.convertToJson(backupData)

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

                backupUseCase.write(content = json, uri = uri)

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
                val json = backupUseCase.read(uri)

                val data = backupUseCase.convertFromJson(json)

                _state.update {
                    it.copy(
                        bufferRestore = data
                    )
                }

            } catch (e: Exception){
                _state.update {
                    it.copy(
                        bufferRestore = null,
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

    private fun requestGoogleDriveAuthorization() {
        viewModelScope.launch {
            _effect.emit(BackupEffect.RequestGoogleDriveAuthorization)
        }
    }

    private fun observeAutoBackupSettings() {
        viewModelScope.launch {
            autoBackupSettingsUseCase.settings
                .collect {settings ->
                    _state.update {
                        it.copy(
                            autoBackupSettings = settings
                        )
                    }
                }
        }
    }

    private fun checkGoogleDriveAuthorization() {
        viewModelScope.launch {
            val isAuthorized = googleDriveUseCase.isAuthorized()

            _state.update {
                it.copy(
                    isGoogleDriveAuthorized = isAuthorized
                )
            }
        }
    }

    private fun disconnectGoogleDrive() {
        viewModelScope.launch {
            try {
                googleDriveUseCase.disconnect()
                _state.update {
                    it.copy(
                        isGoogleDriveAuthorized = false,
                        message = "Google Drive terputus"
                    )
                }
                if(_state.value.autoBackupSettings.enabled){
                    autoBackupSettingsUseCase.setEnabled(false)
                }
            } catch (e: Exception){
                Log.e("GOOGLE_DRIVE", "disconnect gagal", e)
                _state.update {
                    it.copy(
                        message = "Gagal memutus koneksi Google Drive"
                    )
                }
            }
        }
    }

}