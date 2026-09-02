package com.example.uangku.feature.period.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.periodDataStore by preferencesDataStore(
    name = "period_preferences"
)

class PeriodDataStore (

    private val context: Context

) {
    private object Keys {
        val PERIOD_START_DAY = intPreferencesKey(
            "period_start_day"
        )
    }

    val periodStartDay: Flow<Int>
        get() = context.periodDataStore.data.map { preferences ->
            preferences[Keys.PERIOD_START_DAY] ?: 1
        }

    suspend fun setPeriodStartDay(startDay: Int) {
        context.periodDataStore.edit { preferences ->
            preferences[Keys.PERIOD_START_DAY] = startDay
        }
    }
}