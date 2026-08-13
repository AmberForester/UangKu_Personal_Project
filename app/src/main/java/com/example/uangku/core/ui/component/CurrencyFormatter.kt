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