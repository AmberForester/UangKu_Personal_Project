package com.example.uangku.feature.budget.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.uangku.feature.budget.domain.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT *" +
            "FROM budget_view " +
            "ORDER BY categoryName ASC ")
    fun getBudgets(): Flow<List<BudgetView>>

    @Query("SELECT * FROM budget_view WHERE id = :id")
    suspend fun getBudgetById(id: Long) : BudgetView?

    @Query("SELECT * FROM budget_view WHERE categoryId = :categoryId")
    suspend fun getBudgetByCategoryId(categoryId: Long) : BudgetView?

    @Insert
    suspend fun createBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
}