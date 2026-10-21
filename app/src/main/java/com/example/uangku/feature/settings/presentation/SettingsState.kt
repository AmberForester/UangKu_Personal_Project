package com.example.uangku.feature.settings.presentation

import com.example.uangku.core.ui.theme.ThemeMode

data class SettingsState (

    val showPeriodDialog: Boolean? = false,
    val themeMode: ThemeMode? = ThemeMode.LIGHT

)