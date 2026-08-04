package com.example.uangku.feature.transaction.domain

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Transaction?

    suspend fun createTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

}