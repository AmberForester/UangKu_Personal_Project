package com.example.uangku.feature.dashboard.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.feature.transaction.domain.TransactionSummary

@Composable
fun SummaryCard(
    summary: TransactionSummary
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = currencyFormatter(summary.balance),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box (
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    SummaryItem(
                        title = "Income",
                        amount = summary.income
                    )
                }

                VerticalDivider(modifier = Modifier.height(50.dp))

                Box (
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    SummaryItem(
                        title = "Expense",
                        amount = summary.expense
                    )
                }

//                SummaryItem(
//                    title = "Income",
//                    amount = summary.income
//                )
//                Spacer(Modifier.weight(1f))
//                VerticalDivider(modifier = Modifier.height(50.dp))
//                Spacer(Modifier.weight(1f))
//
//                SummaryItem(
//                    title = "Expense",
//                    amount = summary.expense
//                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    title: String,
    amount: Double
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = currencyFormatter(amount),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}