package com.example.uangku.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.setting.data.ThemeDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel (

    private val themeDataStore: ThemeDataStore

) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())

    val state = _state.asStateFlow()

    fun onEvent (event: SettingsEvent){
        when(event) {
            is SettingsEvent.OnThemeSelected -> {
                viewModelScope.launch {
                    themeDataStore.setThemeMode(event.selected)

                    _state.update {
                        it.copy(
                            themeMode = event.selected
                        )
                    }
                }
            }
        }
    }
}