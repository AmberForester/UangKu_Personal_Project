package com.example.uangku.feature.dashboard.presentation

import com.example.uangku.feature.transaction.domain.TransactionSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardStateAvailableToSpendTest {

    @Test
    fun availableToSpend_deducts_currentPeriodSavings_from_balance() {
        val state = DashboardState(
            summary = TransactionSummary(
                income = 5000.0,
                expense = 3000.0,
                balance = 2000.0
            ),
            currentPeriodSavings = 500.0
        )

        // Saldo periode yang dapat dibelanjakan: 2000 - 500 = 1500
        assertEquals(1500.0, state.availableToSpend, 0.001)
    }

    @Test
    fun availableToSpend_equals_balance_when_no_savings_in_current_period() {
        val state = DashboardState(
            summary = TransactionSummary(
                income = 2000.0,
                expense = 0.0,
                balance = 2000.0
            ),
            totalSavings = 5000.0, // Total tabungan masa lalu ada 5000
            currentPeriodSavings = 0.0 // Tapi periode ini belum menabung
        )

        // Saldo bulan ini tidak terpotong oleh tabungan periode lalu
        assertEquals(2000.0, state.availableToSpend, 0.001)
    }
}
