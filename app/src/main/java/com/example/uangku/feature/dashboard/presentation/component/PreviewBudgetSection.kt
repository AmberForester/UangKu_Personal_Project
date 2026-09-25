package com.example.uangku.feature.dashboard.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangku.feature.budget.domain.BudgetSummary

@Composable
fun PreviewBudgetSection(
    budgets: List<BudgetSummary>,
    onSeeAllClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ){

        SectionHeader(
         title = "Budget Preview",
            onSeeAllClick = onSeeAllClick
        )

        if(budgets.isEmpty()){
            Text(
                text = "Belum ada budget",
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            budgets.forEach{ budget ->
                if (budget.budgetAmount != 0.0){
                    BudgetCard(
                        budget = budget
                    )
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    budget: BudgetSummary
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp) )
    {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
        ) {

            Text(
                text = budget.categoryName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer( modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                LinearProgressIndicator(
                    progress = { budget.progress },
                    modifier = Modifier
                        .weight(1f) .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color =
                        if (budget.isOverBudget) {
                            MaterialTheme.colorScheme.error
                        } else if (budget.progress >= 0.75f) {
                            Color(0xFFFB6400)
                        } else if (budget.progress >= 0.5f) {
                            Color.Yellow
                        } else {
                            Color(0xFF43A047)
                        }
                )

                Spacer( modifier = Modifier.width(12.dp) )

                Text(
                    text = "${(budget.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold )
            }
        }
    }
}