package com.example.uangku.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uangku.feature.budget.data.BudgetDao
import com.example.uangku.feature.budget.data.BudgetEntity
import com.example.uangku.feature.budget.data.BudgetView
import com.example.uangku.feature.category.data.CategoryDao
import com.example.uangku.feature.category.data.CategoryEntity
import com.example.uangku.feature.savings.data.SavingGoalEntity
import com.example.uangku.feature.savings.data.SavingLogEntity
import com.example.uangku.feature.savings.data.SavingsDao
import com.example.uangku.feature.transaction.data.TransactionDao
import com.example.uangku.feature.transaction.data.TransactionEntity
import com.example.uangku.feature.transaction.data.TransactionView

@Database(
    entities = [
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class,
        SavingGoalEntity::class,
        SavingLogEntity::class
    ],
    views = [
        TransactionView::class,
        BudgetView::class
            ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    CategoryTypeConverter::class,
    DateConverter::class,
    SavingTypeConverter::class
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsDao(): SavingsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `saving_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `targetAmount` REAL, `currentAmount` REAL NOT NULL, `targetDate` INTEGER)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `saving_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `goalId` INTEGER NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `date` INTEGER NOT NULL, `description` TEXT, FOREIGN KEY(`goalId`) REFERENCES `saving_goals`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_saving_logs_goalId` ON `saving_logs` (`goalId`)"
                )
            }
        }
    }
}