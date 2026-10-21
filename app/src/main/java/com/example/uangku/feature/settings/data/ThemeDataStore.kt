package com.example.uangku.feature.setting.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.uangku.core.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore(
    name = "theme_preferences"
)

class ThemeDataStore (
    private val context: Context
){
    companion object {
        private val  THEME_KEY = stringPreferencesKey("app_theme_mode")
    }

    val themeMode: Flow<ThemeMode> = context.themeDataStore.data.map { preferences ->
        val savedTheme = preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(savedTheme)
        } catch (e: Exception){
            ThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: ThemeMode){
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }
}