package com.example.uangku.core.ui.component

import java.text.NumberFormat
import java.util.Locale

fun currencyFormatter(
    amount: Double
): String {

    return NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)

}

fun currencyMinimalFormatter(
    amount: Double
): String {
    val formatter = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    formatter.maximumFractionDigits = 0
    formatter.minimumFractionDigits = 0

    return formatter.format(amount)
}