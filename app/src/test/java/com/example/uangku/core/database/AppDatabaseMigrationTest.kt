package com.example.uangku.core.database

import androidx.sqlite.db.SupportSQLiteDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Proxy

class AppDatabaseMigrationTest {

    @Test
    fun migration_1_to_2_creates_saving_goals_and_saving_logs_tables() {
        val executedSqlList = mutableListOf<String>()

        val fakeDb = Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java)
        ) { _, method, args ->
            if (method.name == "execSQL" && args != null && args.isNotEmpty()) {
                executedSqlList.add(args[0] as String)
            }
            null
        } as SupportSQLiteDatabase

        AppDatabase.MIGRATION_1_2.migrate(fakeDb)

        assertEquals(3, executedSqlList.size)
        assertTrue(executedSqlList.any { it.contains("CREATE TABLE IF NOT EXISTS `saving_goals`") })
        assertTrue(executedSqlList.any { it.contains("CREATE TABLE IF NOT EXISTS `saving_logs`") })
        assertTrue(executedSqlList.any { it.contains("CREATE INDEX IF NOT EXISTS `index_saving_logs_goalId`") })
    }
}
