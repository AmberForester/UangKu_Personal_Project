package com.example.uangku.feature.settings.presentation

import com.example.uangku.core.ui.theme.ThemeMode

interface SettingsEvent {

    data class OnThemeSelected(
        val selected: ThemeMode
    ): SettingsEvent

}