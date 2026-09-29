package com.example.uangku.feature.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.feature.budget.domain.BudgetSummary

@Composable
fun BudgetItem(
    budget: BudgetSummary,
    onEditClick: (BudgetSummary) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
        ) {

            Text(
                text = budget.categoryName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if(budget.budgetAmount == 0.0) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "No Budget",
                        style = MaterialTheme.typography.bodySmall
                    )
                    TextButton(
                        onClick = { onEditClick(budget) },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(30.dp)
                        ) {
                        Text(
                            text = "Set Budget",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

            } else {

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    progress = { budget.progress },
                    color =
                        if (budget.isOverBudget) {
                            Color.Red
                        } else if (budget.progress >= 0.75f) {
                            Color(0xFFFB6400)
                        } else if (budget.progress >= 0.5f) {
                            Color.Yellow
                        } else {
                            Color(0xFF43A047)
                        }
                )

                Row (
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ){
                    Text(
                        text = "${currencyFormatter(budget.spent)} / ${currencyFormatter(budget.budgetAmount)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "${(budget.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if(!budget.isOverBudget){
                    Text(
                        text = "Remaining ${currencyFormatter(budget.remaining)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "Over Budget ${currencyFormatter(budget.remaining)}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(30.dp)
                        .padding(bottom = 8.dp),
                    onClick = { onEditClick(budget) },
                    contentPadding = PaddingValues(0.dp),

                ) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.titleSmall

                    )
                }
            }
        }
    }
}