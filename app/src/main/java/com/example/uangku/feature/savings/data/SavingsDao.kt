package com.example.uangku.feature.savings.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {

    @Query("SELECT * FROM saving_goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<SavingGoalEntity>>

    @Query("SELECT * FROM saving_goals WHERE id = :id LIMIT 1")
    fun getGoalById(id: Long): Flow<SavingGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertGoal(goal: SavingGoalEntity)

    @Update
    suspend fun updateGoal(goal: SavingGoalEntity)

    @Query("DELETE FROM saving_goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)

    @Query("SELECT COALESCE(SUM(currentAmount), 0.0) FROM saving_goals")
    fun getTotalSavings(): Flow<Double>

    @Query("""
        SELECT sg.* FROM saving_goals sg
        LEFT JOIN saving_logs sl ON sg.id = sl.goalId
        GROUP BY sg.id
        ORDER BY MAX(COALESCE(sl.date, 0)) DESC, sg.id DESC
        LIMIT 1
    """)
    fun getMostRecentActiveGoal(): Flow<SavingGoalEntity?>

    // === LOGS ===
    @Query("SELECT * FROM saving_logs WHERE goalId = :goalId ORDER BY date DESC, id DESC")
    fun getLogsByGoalId(goalId: Long): Flow<List<SavingLogEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertLog(log: SavingLogEntity)

    @Query("DELETE FROM saving_logs WHERE id = :id")
    suspend fun deleteLog(id: Long)
}