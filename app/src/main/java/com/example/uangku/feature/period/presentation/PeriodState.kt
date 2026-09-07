package com.example.uangku.feature.period.presentation

import com.example.uangku.feature.period.domain.FinancialPeriod

data class PeriodState (

    val startDay: Int = 1,
    val selectedStartDay: Int = 1,
    val financialPeriod: FinancialPeriod? = null,
    val isLoading: Boolean = false,
    val error: String? = null

)
