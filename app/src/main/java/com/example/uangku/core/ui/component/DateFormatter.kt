package com.example.uangku.core.ui.component

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val transactionDateFormatter =
    SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

fun Date.toDateFormat(): String {
    return transactionDateFormatter.format(this)
}

fun Date.isSameMonth(
    date: Date
) : Boolean {

    val calendar1 = Calendar.getInstance()
    calendar1.time = this

    val calendar2 = Calendar.getInstance()
    calendar2.time = date

    return calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
}