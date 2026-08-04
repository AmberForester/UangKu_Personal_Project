package com.example.uangku.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.uangku.feature.category.data.CategoryDao
import com.example.uangku.feature.category.data.CategoryEntity
import com.example.uangku.feature.transaction.data.TransactionDao
import com.example.uangku.feature.transaction.data.TransactionEntity
import com.example.uangku.feature.transaction.data.TransactionView

@Database(
    entities = [
        CategoryEntity::class,
        TransactionEntity::class
    ],
    views = [
        TransactionView::class
            ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CategoryTypeConverter::class,
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transcationDao(): TransactionDao

}