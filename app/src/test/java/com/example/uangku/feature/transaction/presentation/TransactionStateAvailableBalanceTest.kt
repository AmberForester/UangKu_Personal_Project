package com.example.uangku.feature.transaction.presentation

import com.example.uangku.feature.transaction.domain.TransactionSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionStateAvailableBalanceTest {

    @Test
    fun availableBalance_deducts_totalSavings_from_overall_balance() {
        val state = TransactionState(
            overallSummary = TransactionSummary(
                income = 10000.0,
                expense = 4000.0,
                balance = 6000.0
            ),
            totalSavings = 1500.0
        )

        // Saldo total yang tersedia: 6000 - 1500 = 4500
        assertEquals(4500.0, state.availableBalance, 0.001)
    }

    @Test
    fun availableBalance_equals_overall_balance_when_no_savings() {
        val state = TransactionState(
            overallSummary = TransactionSummary(
                income = 6000.0,
                expense = 2000.0,
                balance = 4000.0
            ),
            totalSavings = 0.0
        )

        assertEquals(4000.0, state.availableBalance, 0.001)
    }

    @Test
    fun availableMonthlyBalance_deducts_monthlySavings_from_monthly_balance() {
        val state = TransactionState(
            monthlySummary = TransactionSummary(
                income = 5000.0,
                expense = 2000.0,
                balance = 3000.0
            ),
            monthlySavings = 1000.0
        )

        // Saldo bulan ini setelah dipotong tabungan bulan ini: 3000 - 1000 = 2000
        assertEquals(2000.0, state.availableMonthlyBalance, 0.001)
    }

    @Test
    fun availableMonthlyBalance_equals_monthly_balance_when_no_monthly_savings() {
        val state = TransactionState(
            monthlySummary = TransactionSummary(
                income = 5000.0,
                expense = 2000.0,
                balance = 3000.0
            ),
            monthlySavings = 0.0
        )

        assertEquals(3000.0, state.availableMonthlyBalance, 0.001)
    }
}
