package com.example.uangku.feature.savings.data

import com.example.uangku.feature.savings.domain.SavingLog

fun SavingLogEntity.toDomain() : SavingLog {
    return SavingLog(
        id = id,
        goalId = goalId,
        amount = amount,
        type = type,
        date = date,
        description = description
    )
}

fun SavingLog.toEntity() : SavingLogEntity {
    return SavingLogEntity(
        id = id,
        goalId = goalId,
        amount = amount,
        type = type,
        date = date,
        description = description
    )
}