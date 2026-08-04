package com.example.uangku.core.ui.component

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val transactionDateFormatter =
    SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

fun Date.toDateFormat(): String {
    return transactionDateFormatter.format(this)
}