package com.example.uangku.feature.budget.presentation.component

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.orangeBg
import com.example.uangku.core.ui.theme.orangeComponent
import com.example.uangku.core.ui.theme.redBg
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import com.example.uangku.core.ui.theme.lightOrangeBg
import com.example.uangku.core.ui.theme.lightOrangeComponent
import com.example.uangku.feature.budget.domain.BudgetSummary

@Composable
fun BudgetItem(
    budget: BudgetSummary,
    onEditClick: (BudgetSummary) -> Unit
) {
    fun formatWithSpace(amount: Double): String {
        val formatted = currencyMinimalFormatter(amount)
        return if (formatted.startsWith("Rp") && !formatted.startsWith("Rp ")) {
            formatted.replaceFirst("Rp", "Rp ")
        } else {
            formatted
        }
    }

    val progressColor = when {
        budget.isOverBudget -> redComponent
        budget.progress >= 0.75f -> orangeComponent
        budget.progress >= 0.5f -> lightOrangeComponent
        else -> GreenComponent
    }

    val boxBgColor = when {
        budget.isOverBudget -> redBg
        budget.progress >= 0.75f -> orangeBg
        budget.progress >= 0.5f -> lightOrangeBg
        else -> greenBg
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = secondaryBgSkyBlue,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header Baris: Kategori & Aksi/Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = budget.categoryName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (budget.budgetAmount > 0.0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge Persentase
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

                        Spacer(Modifier.width(6.dp))

                        // Tombol Edit Ikon Kecil
                        IconButton(
                            onClick = { onEditClick(budget) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Anggaran",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            if (budget.budgetAmount == 0.0) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Belum ada anggaran bulanan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.5.sp
                        ),
                        color = Color(0xFF64748B)
                    )

                    OutlinedButton(
                        onClick = { onEditClick(budget) },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = mainSkyBlue
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = mainSkyBlue.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Atur",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar Halus
                LinearProgressIndicator(
                    progress = { budget.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = progressColor,
                    trackColor = secondaryBgSkyBlue
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Baris Info: Terpakai / Total di kiri, Sisa / Overbudget di kanan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatWithSpace(budget.spent)} / ${formatWithSpace(budget.budgetAmount)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val remainingText = if (budget.isOverBudget) {
                        "Lebih ${formatWithSpace(-budget.remaining)}"
                    } else {
                        "Sisa ${formatWithSpace(budget.remaining)}"
                    }

                    Text(
                        text = remainingText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = if (budget.isOverBudget) redComponent else Color(0xFF334155),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
