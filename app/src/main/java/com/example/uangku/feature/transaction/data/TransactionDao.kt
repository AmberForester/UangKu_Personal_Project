package com.example.uangku.feature.transaction.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * " +
            "FROM transaction_view " +
            "ORDER BY date DESC")
    fun getTransactions(): Flow<List<TransactionView>>

    @Query("SELECT * FROM `transaction_view` WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionView?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

}