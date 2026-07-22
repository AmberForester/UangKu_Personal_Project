package com.example.uangku.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.uangku.feature.category.data.CategoryDao
import com.example.uangku.feature.category.data.CategoryEntity

@Database(
    entities = [
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CategoryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

}