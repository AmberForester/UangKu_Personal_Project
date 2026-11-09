package com.example.uangku.feature.savings.domain

import kotlinx.coroutines.flow.Flow

interface SavingsRepository {

    //Goals
    fun getAllGoals(): Flow<List<SavingGoal>>

    fun getGoalById(id: Long): Flow<SavingGoal?>

    suspend fun createGoal(goal: SavingGoal)

    suspend fun updateGoal(goal: SavingGoal)

    suspend fun deleteGoal(id: Long)

    // Log
    fun getLogsByGoalId(goalId: Long): Flow<List<SavingLog>>

    suspend fun insertLog(log: SavingLog)

    suspend fun deleteLog(id: Long)

    fun getTotalSavings(): Flow<Double>

    fun getMostRecentActiveGoal(): Flow<SavingGoal?>
}