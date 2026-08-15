package com.example.uangku.feature.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uangku.feature.budget.domain.BudgetOverview
import com.example.uangku.core.ui.component.OverviewItem

@Composable
fun BudgetOverviewItem(

    overview: BudgetOverview

) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Monthly Overview",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OverviewItem(
                title = "Income",
                amount = overview.income
            )

            OverviewItem(
                title = "Allocated",
                amount = overview.allocated
            )

            OverviewItem(
                title = "Available",
                amount = overview.available
            )
        }
    }
}