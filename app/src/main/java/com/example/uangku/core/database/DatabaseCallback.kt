package com.example.uangku.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uangku.feature.category.data.DefaultCategories

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.beginTransaction()

        try {

            DefaultCategories.categories.forEach { category ->

                db.execSQL(
                    "INSERT INTO categories (name, type) VALUES (?, ?)",
                    arrayOf(
                        category.name,
                        category.type.name
                    )
                )
            }

            db.setTransactionSuccessful()

        } finally {
            db.endTransaction()
        }
    }
}