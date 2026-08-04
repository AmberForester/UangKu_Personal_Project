package com.example.uangku.feature.transaction.domain

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.feature.category.domain.CategoryRepository
import kotlinx.coroutines.flow.Flow

class TransactionUseCase (
    private val repository: TransactionRepository
){

    fun getTransactions(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return repository.getTransactionById(id)
    }

    suspend fun createTransaction(transaction: Transaction) {
        return repository.createTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        return repository.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        return repository.deleteTransaction(transaction)
    }

}