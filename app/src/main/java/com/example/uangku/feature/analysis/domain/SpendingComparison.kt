package com.example.uangku.feature.analysis.domain

data class SpendingComparison(
    val currentAmount: Double,
    val previousAmount: Double,
    val percentageChange: Int, // misal 12 (%)
    val isPositive: Boolean,    // true jika tren positif: pengeluaran lebih hemat, atau pemasukan lebih tinggi
    val hasPreviousData: Boolean // true jika bulan lalu ada transaksi (> 0)
) {
    val currentExpense: Double get() = currentAmount
    val previousExpense: Double get() = previousAmount
    val isCheaper: Boolean get() = isPositive
}
