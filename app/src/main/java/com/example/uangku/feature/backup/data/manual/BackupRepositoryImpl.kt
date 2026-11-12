package com.example.uangku.feature.backup.data.manual

import androidx.room.withTransaction
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.feature.backup.domain.manual.BackupData
import com.example.uangku.feature.backup.domain.manual.BackupRepository
import com.example.uangku.feature.category.data.toDomain
import com.example.uangku.feature.category.data.toEntity
import com.example.uangku.feature.savings.data.toDomain
import com.example.uangku.feature.savings.data.toEntity
import com.example.uangku.feature.transaction.data.toDomain
import com.example.uangku.feature.transaction.data.toEntity
import kotlinx.coroutines.flow.first

class BackupRepositoryImpl (

    private val database: AppDatabase,

) : BackupRepository {

    override suspend fun getBackupData(): BackupData {
        val categories = database.categoryDao()
            .getCategories().first().map { it.toDomain() }
        val transactions = database.transactionDao()
            .getTransactions().first().map { it.toDomain() }

        val savingGoals = database.savingsDao()
            .getAllGoals().first().map { it.toDomain() }
        val savingLogs = database.savingsDao()
            .getAllLogs().first().map { it.toDomain() }

        return BackupData(
            version = 2,
            categories = categories,
            transactions = transactions,
            savingGoals = savingGoals,
            savingLogs = savingLogs
        )
    }

    override suspend fun restoreData(backupData: BackupData) {
        database.withTransaction {

            database.transactionDao().deleteAllTransactions()

            database.categoryDao().deleteAllCategories()

            database.savingsDao().deleteAllLogs()

            database.savingsDao().deleteAllGoals()

            database.categoryDao().insertCategories(
                backupData.categories.map { it.toEntity() }
            )

            database.transactionDao().insertTransactions(
                backupData.transactions.map { it.toEntity() }
            )

            database.savingsDao().insertGoals(
                backupData.savingGoals.map { it.toEntity() }
            )

            database.savingsDao().insertLogs(
                backupData.savingLogs.map { it.toEntity() }
            )
        }
    }
}