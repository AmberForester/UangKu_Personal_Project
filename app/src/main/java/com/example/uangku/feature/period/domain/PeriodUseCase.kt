package com.example.uangku.feature.period.domain

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
class PeriodUseCase (
    private val repository: PeriodRepository
){
    fun getPeriodSettings(): Flow<PeriodSettings> {

        return repository.getPeriodSettings()

    }

    suspend fun updateStartDay(startDay: Int) {

        require(startDay in 1..31) {
            "Start day must be between 1 and 31"
        }
        repository.updateStartDay(startDay)
    }

    suspend fun getFinancialPeriod(
        date: LocalDate = LocalDate.now()
    ) : FinancialPeriod {

        val periodSettings = repository.getPeriodSettings().first()

        val startDay = periodSettings.startDay

        val startDate = getStartDate(
            date = date,
            startDay = startDay
        )

        val nextPeriodStartDate = getNextPeriodStartDate(
            startDate = startDate,
            startDay = startDay
        )

        return FinancialPeriod(
            startDate = startDate,
            endDate = nextPeriodStartDate.minusDays(1)
        )
    }

    private fun getStartDate(

        date: LocalDate,
        startDay: Int

    ): LocalDate {

        val currentMonthStartDay = min(
            startDay,
            date.lengthOfMonth()
        )

        return if (date.dayOfMonth >= currentMonthStartDay) {

            date.withDayOfMonth(currentMonthStartDay)

        } else {

            val previousMonth = date.minusMonths(1)

            val previousMonthStartDay = min(
                startDay,
                previousMonth.lengthOfMonth()
            )

            previousMonth.withDayOfMonth(
                previousMonthStartDay
            )
        }
    }

    private fun getNextPeriodStartDate(
        startDate: LocalDate,
        startDay: Int
    ): LocalDate {

        val nextMonth = startDate.plusMonths(1)

        val nextMonthStartDay = min(
            startDay,
            nextMonth.lengthOfMonth()
        )

        return nextMonth.withDayOfMonth(
            nextMonthStartDay
        )
    }
}