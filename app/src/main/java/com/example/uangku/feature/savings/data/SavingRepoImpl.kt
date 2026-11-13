package com.example.uangku.feature.savings.data

import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingLog
import com.example.uangku.feature.savings.domain.SavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavingRepoImpl (
    private val savingsDao: SavingsDao
) : SavingsRepository {

    override fun getAllGoals(): Flow<List<SavingGoal>> {
        return savingsDao.getAllGoals().map { goals ->
            goals.map { it.toDomain() }
        }
    }

    override fun getGoalById(id: Long): Flow<SavingGoal?> {
        return savingsDao.getGoalById(id).map { it?.toDomain() }
    }

    override suspend fun createGoal(goal: SavingGoal) {
        savingsDao.insertGoal(goal.toEntity())
    }

    override suspend fun updateGoal(goal: SavingGoal) {
        savingsDao.updateGoal(goal.toEntity())
    }

    override suspend fun deleteGoal(id: Long) {
        savingsDao.deleteGoal(id)
    }

    override fun getLogsByGoalId(goalId: Long): Flow<List<SavingLog>> {
        return savingsDao.getLogsByGoalId(goalId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllLogs(): Flow<List<SavingLog>> {
        return savingsDao.getAllLogs().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertLog(log: SavingLog) {
        savingsDao.insertLog(log.toEntity())
    }

    override suspend fun deleteLog(id: Long) {
        savingsDao.deleteLog(id)
    }

    override fun getTotalSavings(): Flow<Double> {
        return savingsDao.getTotalSavings()
    }

    override fun getMostRecentActiveGoal(): Flow<SavingGoal?> {
        return savingsDao.getMostRecentActiveGoal().map { it?.toDomain() }
    }
}
