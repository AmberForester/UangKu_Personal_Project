package com.example.uangku.feature.savings.domain

import com.example.uangku.core.database.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class SavingLog(

    val id: Long? = null,
    val goalId: Long,
    val amount: Double,
    val type: SavingType,

    @Serializable(with = DateSerializer::class)
    val date: Date = Date(),

    val description: String? = null

)
