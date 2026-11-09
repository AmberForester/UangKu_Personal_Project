package com.example.uangku.feature.dashboard.presentation

import com.example.uangku.feature.transaction.domain.TransactionSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardStateAvailableToSpendTest {

    @Test
    fun availableToSpend_deducts_totalSavings_from_balance() {
        val state = DashboardState(
            summary = TransactionSummary(
                income = 5000.0,
                expense = 3000.0,
                balance = 2000.0
            ),
            totalSavings = 500.0
        )

        // Saldo yang dapat dibelanjakan: 2000 - 500 = 1500
        assertEquals(1500.0, state.availableToSpend, 0.001)
    }

    @Test
    fun availableToSpend_equals_balance_when_no_savings() {
        val state = DashboardState(
            summary = TransactionSummary(
                income = 2000.0,
                expense = 0.0,
                balance = 2000.0
            ),
            totalSavings = 0.0
        )

        assertEquals(2000.0, state.availableToSpend, 0.001)
    }
}
