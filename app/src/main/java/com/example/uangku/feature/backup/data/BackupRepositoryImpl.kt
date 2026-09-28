package com.example.uangku.feature.backup.data

import androidx.room.withTransaction
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.feature.backup.domain.BackupData
import com.example.uangku.feature.backup.domain.BackupRepository
import com.example.uangku.feature.category.data.toDomain
import com.example.uangku.feature.category.data.toEntity
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

        return BackupData(
            categories = categories,
            transactions = transactions
        )
    }

    override suspend fun restoreData(backupData: BackupData) {
        database.withTransaction {

            database.transactionDao().deleteAllTransactions()

            database.categoryDao().deleteAllCategories()

            database.categoryDao().insertCategories(
                backupData.categories.map { it.toEntity() }
            )

            database.transactionDao().insertTransactions(
                backupData.transactions.map { it.toEntity() }
            )
        }
    }
}