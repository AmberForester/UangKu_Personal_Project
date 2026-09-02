package com.example.uangku.feature.period.data

import com.example.uangku.feature.period.domain.PeriodRepository
import com.example.uangku.feature.period.domain.PeriodSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PeriodRepositoryImpl (
    private val dataStore: PeriodDataStore
) : PeriodRepository {

    override fun getPeriodSettings(): Flow<PeriodSettings> {
        return dataStore.periodStartDay.map { startDay ->
            PeriodSettings( startDay )
        }
    }

    override suspend fun updateStartDay(startDay: Int) {
        dataStore.setPeriodStartDay(startDay)
    }


}