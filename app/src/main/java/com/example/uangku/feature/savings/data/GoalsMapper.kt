package com.example.uangku.feature.savings.data

import com.example.uangku.feature.savings.domain.SavingGoal

fun SavingGoalEntity.toDomain() : SavingGoal{
    return SavingGoal(
        id = id,
        title = title,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        targetDate = targetDate
    )
}

fun SavingGoal.toEntity() : SavingGoalEntity {
    return SavingGoalEntity(
        id = id,
        title = title,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        targetDate = targetDate,
    )
}