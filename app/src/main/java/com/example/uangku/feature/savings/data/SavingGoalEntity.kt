package com.example.uangku.feature.savings.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "saving_goals")
data class SavingGoalEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val title: String,

    val targetAmount: Double? = null,

    val currentAmount: Double = 0.0,

    val targetDate: Date? = null
)