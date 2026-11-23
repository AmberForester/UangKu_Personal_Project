package com.example.uangku.feature.transaction.presentation

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.Category
import com.example.uangku.feature.transaction.domain.Transaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class TransactionStateFilterTest {

    private val sampleTransactions = listOf(
        Transaction(
            id = 1L,
            description = "Makan Siang",
            type = Type.EXPENSE,
            amount = 25000.0,
            categoryId = 1,
            categoryName = "Makanan",
            date = Date()
        ),
        Transaction(
            id = 2L,
            description = "Beli Bensin",
            type = Type.EXPENSE,
            amount = 50000.0,
            categoryId = 2,
            categoryName = "Transportasi",
            date = Date()
        ),
        Transaction(
            id = 3L,
            description = "Gaji Bulanan",
            type = Type.INCOME,
            amount = 5000000.0,
            categoryId = 3,
            categoryName = "Gaji",
            date = Date()
        ),
        Transaction(
            id = 4L,
            description = "Bonus Proyek",
            type = Type.INCOME,
            amount = 1000000.0,
            categoryId = 4,
            categoryName = "Bonus",
            date = Date()
        )
    )

    @Test
    fun filteredTransactions_returns_all_when_no_filter() {
        val state = TransactionState(transactions = sampleTransactions)
        assertEquals(4, state.filteredTransactions.size)
        assertFalse(state.hasActiveFilter)
    }

    @Test
    fun filteredTransactions_filters_by_expense_type() {
        val state = TransactionState(
            transactions = sampleTransactions,
            selectedType = Type.EXPENSE
        )
        val filtered = state.filteredTransactions
        assertEquals(2, filtered.size)
        assertTrue(filtered.all { it.type == Type.EXPENSE })
        assertTrue(state.hasActiveFilter)
    }

    @Test
    fun filteredTransactions_filters_by_income_type() {
        val state = TransactionState(
            transactions = sampleTransactions,
            selectedType = Type.INCOME
        )
        val filtered = state.filteredTransactions
        assertEquals(2, filtered.size)
        assertTrue(filtered.all { it.type == Type.INCOME })
        assertTrue(state.hasActiveFilter)
    }

    @Test
    fun filteredTransactions_filters_by_type_and_category() {
        val state = TransactionState(
            transactions = sampleTransactions,
            selectedType = Type.EXPENSE,
            selectedCategory = Category(id = 1L, name = "Makanan", type = Type.EXPENSE)
        )
        val filtered = state.filteredTransactions
        assertEquals(1, filtered.size)
        assertEquals("Makan Siang", filtered.first().description)
        assertTrue(state.hasActiveFilter)
    }
}
