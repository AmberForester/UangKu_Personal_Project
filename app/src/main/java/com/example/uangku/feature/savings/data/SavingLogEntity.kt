package com.example.uangku.feature.savings.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.uangku.feature.savings.domain.SavingType
import java.util.Date

@Entity(
    tableName = "saving_logs",
    foreignKeys = [
        ForeignKey(
            entity = SavingGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["goalId"])]
)
data class SavingLogEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val goalId: Long,

    val amount: Double,

    val type: SavingType,

    val date: Date = Date(),

    val description: String? = null
)