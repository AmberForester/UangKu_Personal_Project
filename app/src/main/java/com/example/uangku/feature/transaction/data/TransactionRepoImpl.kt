package com.example.uangku.feature.transaction.data

import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepoImpl(
    private val transactionDao: TransactionDao

) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getTransactions()
            .map { transaction ->
                transaction.map { it.toDomain() }
            }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    override suspend fun createTransaction(transaction: Transaction) {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        return transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        return transactionDao.deleteTransaction(transaction.toEntity())
    }
}