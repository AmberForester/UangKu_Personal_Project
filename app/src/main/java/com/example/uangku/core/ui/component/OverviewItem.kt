package com.example.uangku.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OverviewItem(
    title: String,
    amount: Double,
    type: String = ""
) {
    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = if (type.equals("min", ignoreCase = true)){
                currencyMinimalFormatter(amount)
            } else {
                currencyFormatter(amount)
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}