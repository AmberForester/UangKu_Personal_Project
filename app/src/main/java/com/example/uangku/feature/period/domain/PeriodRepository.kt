package com.example.uangku.feature.period.domain

import kotlinx.coroutines.flow.Flow

interface PeriodRepository {

    fun getPeriodSettings(): Flow<PeriodSettings>

    suspend fun updateStartDay(startDay: Int)
}