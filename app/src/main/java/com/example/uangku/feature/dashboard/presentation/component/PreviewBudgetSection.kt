package com.example.uangku.feature.dashboard.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.core.ui.theme.lightOrangeBg
import com.example.uangku.core.ui.theme.lightOrangeComponent
import com.example.uangku.core.ui.theme.orangeBg
import com.example.uangku.core.ui.theme.orangeComponent
import com.example.uangku.core.ui.theme.redBg
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.feature.budget.domain.BudgetSummary

@Composable
fun PreviewBudgetSection(
    budgets: List<BudgetSummary>,
    onSeeAllClick: () -> Unit,
    onBudgetClick: ((Int) -> Unit)? = null
) {
    val activeBudgets = budgets.filter { it.budgetAmount != 0.0 }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            SectionHeader(
                title = "Ringkasan Anggaran",
                onSeeAllClick = onSeeAllClick
            )
        }

        Spacer(Modifier.height(8.dp))

        if (activeBudgets.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada budget diatur",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activeBudgets, key = { it.categoryId }) { budget ->
                    BudgetCard(
                        budget = budget,
                        onClick = onBudgetClick?.let { { it(budget.categoryId) } }
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    budget: BudgetSummary,
    onClick: (() -> Unit)? = null
) {
    val isDark = MaterialTheme.colorScheme.background != com.example.uangku.core.ui.theme.mainBgSkyBlue
    val isBudgetMaxedOrOver = budget.percentage >= 100 || budget.isOverBudget || budget.progress >= 1f

    val progressColor = when {
        isBudgetMaxedOrOver -> redComponent
        budget.progress >= 0.75f -> orangeComponent
        budget.progress >= 0.5f -> lightOrangeComponent
        else -> GreenComponent
    }

    val boxBgColor = if (isDark) {
        progressColor.copy(alpha = 0.2f)
    } else {
        when {
            isBudgetMaxedOrOver -> redBg
            budget.progress >= 0.75f -> orangeBg
            budget.progress >= 0.5f -> lightOrangeBg
            else -> greenBg
        }
    }

    Card(
        modifier = Modifier
            .width(220.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header Baris: Nama Kategori & Badge Persentase
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = budget.categoryName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(boxBgColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${budget.percentage}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar dengan Track Halus
            LinearProgressIndicator(
                progress = { budget.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Nominal Pengeluaran / Total Anggaran
            val spentText = currencyMinimalFormatter(budget.spent).let {
                if (it.startsWith("Rp") && !it.startsWith("Rp ")) it.replaceFirst("Rp", "Rp ") else it
            }
            val totalText = currencyMinimalFormatter(budget.budgetAmount).let {
                if (it.startsWith("Rp") && !it.startsWith("Rp ")) it.replaceFirst("Rp", "Rp ") else it
            }

            Text(
                text = "$spentText / $totalText",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
