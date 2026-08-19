package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.OverviewItem
import com.example.uangku.feature.transaction.domain.TransactionSummary

@Composable
fun ShowTransactionSummary(
    monthlySummary: TransactionSummary,
    overallSummary: TransactionSummary
) {
    val size by remember { mutableStateOf(12) }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OverviewItem(
                title = "Overall",
                amount = overallSummary.balance,
                type = "min",
                size = size
            )

            OverviewItem(
                title = "Income",
                amount = monthlySummary.income,
                type = "min",
                size = size
            )

            OverviewItem(
                title = "Expense",
                amount = monthlySummary.expense,
                type = "min",
                size = size
            )

            OverviewItem(
                title = "Balance",
                amount = monthlySummary.balance,
                type = "min",
                size = size
            )
        }
    }
}