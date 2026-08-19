package com.example.uangku.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun OverviewItem(
    title: String,
    amount: Double,
    type: String = "",
    size: Int = 0
) {
    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = if(size != 0) size.sp else TextUnit.Unspecified

        )

        Text(
            text = if (type.equals("min", ignoreCase = true)){
                currencyMinimalFormatter(amount)
            } else {
                currencyFormatter(amount)
            },
            style = MaterialTheme.typography.bodySmall,
            fontSize = if(size != 0) size.sp else TextUnit.Unspecified

        )
    }
}