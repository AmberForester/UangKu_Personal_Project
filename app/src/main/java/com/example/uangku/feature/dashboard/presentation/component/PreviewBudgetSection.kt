package com.example.uangku.feature.dashboard.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.activeGreen
import com.example.uangku.core.ui.theme.budgetBorderColor
import com.example.uangku.core.ui.theme.budgetGreenBg
import com.example.uangku.core.ui.theme.budgetOrangeBg
import com.example.uangku.core.ui.theme.budgetOrangeText
import com.example.uangku.core.ui.theme.budgetRedBg
import com.example.uangku.core.ui.theme.budgetRedText
import com.example.uangku.core.ui.theme.budgetTrackColor
import com.example.uangku.core.ui.theme.budgetYellowBg
import com.example.uangku.core.ui.theme.budgetYellowText
import com.example.uangku.feature.budget.domain.BudgetSummary

@Composable
fun PreviewBudgetSection(
    budgets: List<BudgetSummary>,
    onSeeAllClick: () -> Unit
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
                        color = budgetBorderColor,
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
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
                        color = Color(0xFF64748B)
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
                    BudgetCard(budget = budget)
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    budget: BudgetSummary
) {
    val progressColor = when {
        budget.isOverBudget -> budgetRedText
        budget.progress >= 0.75f -> budgetOrangeText
        budget.progress >= 0.5f -> budgetYellowText
        else -> activeGreen
    }

    val boxBgColor = when {
        budget.isOverBudget -> budgetRedBg
        budget.progress >= 0.75f -> budgetOrangeBg
        budget.progress >= 0.5f -> budgetYellowBg
        else -> budgetGreenBg
    }

    Card(
        modifier = Modifier
            .width(220.dp)
            .border(
                width = 1.dp,
                color = budgetBorderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
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
                    color = Color(0xFF0F172A),
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
                        text = "${(budget.progress * 100).toInt()}%",
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
                trackColor = budgetTrackColor
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
                color = Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
