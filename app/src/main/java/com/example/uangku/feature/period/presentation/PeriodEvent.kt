package com.example.uangku.feature.period.presentation

interface PeriodEvent {

    data object  onScreenOpen : PeriodEvent

    data class onStartDayChange(
        val startDay: Int
    ): PeriodEvent

    data object onSave : PeriodEvent
}