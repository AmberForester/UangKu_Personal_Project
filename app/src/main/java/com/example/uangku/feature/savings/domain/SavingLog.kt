package com.example.uangku.feature.savings.domain

import java.util.Date

data class SavingLog(

    val id: Long? = null,
    val goalId: Long,
    val amount: Double,
    val type: SavingType,
    val date: Date = Date(),
    val description: String? = null

)
