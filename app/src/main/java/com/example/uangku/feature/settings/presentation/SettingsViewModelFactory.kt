package com.example.uangku.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.setting.data.ThemeDataStore

class SettingsViewModelFactory(

    private val themeDataStore: ThemeDataStore

): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(themeDataStore) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}