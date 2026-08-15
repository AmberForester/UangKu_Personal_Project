package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.OverviewItem
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.feature.transaction.domain.TransactionSummary

@Composable
fun ShowTransactionSummary(
    monthlySummary: TransactionSummary,
    overallSummary: TransactionSummary
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 15.dp)
    ) {

        Text(
            text = "Current Balance",
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = currencyFormatter(overallSummary.balance),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Monthly Summary",
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OverviewItem(
                title = "Income",
                amount = monthlySummary.income,
                type = "min"
            )

            OverviewItem(
                title = "Expense",
                amount = monthlySummary.expense,
                type = "min"
            )

            OverviewItem(
                title = "Balance",
                amount = monthlySummary.balance,
                type = "min"
            )
        }
    }
}