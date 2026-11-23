package com.example.uangku.feature.savings.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.feature.period.domain.FinancialPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.util.Date

class SavingsUseCase (
    private val savingsRepository: SavingsRepository
) {
    fun getAllGoals(): Flow<List<SavingGoal>> {
        return savingsRepository.getAllGoals()
    }

    fun getAllLogs(): Flow<List<SavingLog>> {
        return savingsRepository.getAllLogs()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSavedAmountForPeriod(period: FinancialPeriod): Flow<Double> {
        val zoneId = ZoneId.systemDefault()
        val startDate = Date.from(
            period.startDate
                .atStartOfDay(zoneId)
                .toInstant()
        )
        val endDate = Date.from(
            period.endDate
                .plusDays(1)
                .atStartOfDay(zoneId)
                .toInstant()
        )

        return savingsRepository.getAllLogs().map { logs ->
            val periodLogs = logs.filter { it.date >= startDate && it.date < endDate }
            val deposits = periodLogs.filter { it.type == SavingType.DEPOSIT }.sumOf { it.amount }
            val withdrawals = periodLogs.filter { it.type == SavingType.WITHDRAW }.sumOf { it.amount }
            (deposits - withdrawals).coerceAtLeast(0.0)
        }
    }

    fun getGoalById(id: Long): Flow<SavingGoal?> {
        return savingsRepository.getGoalById(id)
    }

    fun getLogsByGoalId(goalId: Long): Flow<List<SavingLog>> {
        return savingsRepository.getLogsByGoalId(goalId)
    }

    fun getTotalLockedSavings(): Flow<Double> {
        return savingsRepository.getTotalSavings()
    }

    fun getMostRecentActiveGoal(): Flow<SavingGoal?> {
        return savingsRepository.getMostRecentActiveGoal()
    }

    suspend fun createGoal(goal: SavingGoal) {
        require(goal.title.isNotBlank()) { "Nama tabungan tidak boleh kosong" }
        goal.targetAmount?.let {
            require(it > 0) { "Target nominal harus lebih besar dari 0" }
        }
        savingsRepository.createGoal(goal)
    }

    suspend fun updateGoal(goal: SavingGoal) {
        require(goal.title.isNotBlank()) { "Nama tabungan tidak boleh kosong" }
        goal.targetAmount?.let {
            require(it > 0) { "Target nominal harus lebih besar dari 0" }
        }
        savingsRepository.updateGoal(goal)
    }

    suspend fun deleteGoal(goalId: Long) {
        savingsRepository.deleteGoal(goalId)
    }

    suspend fun deposit (
        goalId: Long,
        amount: Double,
        desc: String? = null
    ) {
        require(amount > 0) { "Nominal setor harus lebih besar dari 0" }

        val currentGoal = savingsRepository.getGoalById(goalId).first()
            ?: throw IllegalStateException("Goal dengan id $goalId tidak ditemukan")

        val newAmount = currentGoal.currentAmount + amount

        val log = SavingLog(
            goalId = goalId,
            amount = amount,
            type = SavingType.DEPOSIT,
            date = Date(),
            description = desc
        )

        val updatedGoal = currentGoal.copy(currentAmount = newAmount)

        savingsRepository.updateGoal(updatedGoal)
        savingsRepository.insertLog(log)
    }

    suspend fun withdraw (
        goalId: Long,
        amount: Double,
        desc: String? = null
    ){
        require(amount > 0) { "Nominal tarik harus lebih besar dari 0" }

        val currentGoal = savingsRepository.getGoalById(goalId).first()
            ?: throw IllegalStateException("Goal dengan id $goalId tidak ditemukan")

        require(amount <= currentGoal.currentAmount) {
            "Saldo tidak mencukupi untuk melakukan penarikan"
        }

        val newAmount = currentGoal.currentAmount - amount

        val log = SavingLog(
            goalId = goalId,
            amount = amount,
            type = SavingType.WITHDRAW,
            date = Date(),
            description = desc
        )

        val updatedGoal = currentGoal.copy(currentAmount = newAmount)

        savingsRepository.updateGoal(updatedGoal)
        savingsRepository.insertLog(log)
    }
}