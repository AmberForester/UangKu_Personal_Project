package com.example.uangku.feature.savings.domain

import java.util.Date

data class SavingGoal (

    val id: Long? = null,
    val title: String,
    val targetAmount: Double? = null,
    val currentAmount: Double = 0.0,
    val targetDate: Date? = null
) {
    val progress: Float?
        get() = targetAmount?.let { target ->
            if(target > 0){
                (currentAmount / target).toFloat().coerceIn(0f, 1f)
            } else {
                null
            }
        }

    val remaining: Double?
        get() = targetAmount?.let { target ->
            (target - currentAmount).coerceAtLeast(0.0)
        }

    val isTargetReached: Boolean
        get() = targetAmount?.let { target -> target > 0 && currentAmount >= target } ?: false
}