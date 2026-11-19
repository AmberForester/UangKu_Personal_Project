package com.example.uangku.feature.analysis.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.analysis.domain.SpendingInsights
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AnalysisInsightCard(
    modifier: Modifier = Modifier,
    insights: SpendingInsights,
    hasTransactions: Boolean,
    selectedType: Type = Type.EXPENSE,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
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
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Baris Atas: Perbandingan dengan Bulan Lalu
            val comparison = insights.comparison
            val (badgeIcon, badgeColor, badgeText) = when {
                !hasTransactions -> Triple(
                    Icons.Default.Info,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    if (selectedType == Type.EXPENSE) {
                        "Belum ada transaksi pengeluaran periode ini"
                    } else {
                        "Belum ada transaksi pemasukan periode ini"
                    }
                )
                comparison != null && comparison.hasPreviousData && comparison.isPositive -> Triple(
                    if (selectedType == Type.EXPENSE) Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingUp,
                    successGreen,
                    if (selectedType == Type.EXPENSE) {
                        "${comparison.percentageChange}% Lebih hemat dibanding bulan lalu"
                    } else {
                        "${comparison.percentageChange}% Lebih tinggi dibanding bulan lalu"
                    }
                )
                comparison != null && comparison.hasPreviousData && !comparison.isPositive -> Triple(
                    if (selectedType == Type.EXPENSE) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                    generalRed,
                    if (selectedType == Type.EXPENSE) {
                        "${comparison.percentageChange}% Lebih tinggi dibanding bulan lalu"
                    } else {
                        "${comparison.percentageChange}% Lebih rendah dibanding bulan lalu"
                    }
                )
                else -> Triple(
                    Icons.Default.Info,
                    MaterialTheme.colorScheme.primary,
                    if (selectedType == Type.EXPENSE) {
                        "Belum ada data pengeluaran di bulan lalu"
                    } else {
                        "Belum ada data pemasukan di bulan lalu"
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = badgeColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor
                )
            }

            if (hasTransactions) {
                Spacer(Modifier.height(14.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    thickness = 1.dp
                )

                Spacer(Modifier.height(14.dp))

                // 2. Tiga Poin Fakta Menarik
                InsightStatRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Rata-rata Harian",
                    value = "${currencyMinimalFormatter(insights.dailyAverage)} / hari"
                )

                Spacer(Modifier.height(10.dp))

                val peakDayText = if (insights.highestSpendingDate != null && insights.highestSpendingAmount > 0) {
                    val dateFormat = SimpleDateFormat("d MMM", Locale("id", "ID"))
                    "${currencyMinimalFormatter(insights.highestSpendingAmount)} (${dateFormat.format(insights.highestSpendingDate)})"
                } else {
                    "-"
                }

                val peakLabel = if (selectedType == Type.EXPENSE) "Pengeluaran Tertinggi" else "Pemasukan Tertinggi"
                InsightStatRow(
                    icon = Icons.Default.FlashOn,
                    label = peakLabel,
                    value = peakDayText
                )

                Spacer(Modifier.height(10.dp))

                val topCategoryText = if (!insights.topCategoryName.isNullOrBlank()) {
                    "${insights.topCategoryName} (${insights.topCategoryPercentage}%)"
                } else {
                    "-"
                }

                val topCategoryLabel = if (selectedType == Type.EXPENSE) "Alokasi Terbesar" else "Sumber Terbesar"
                InsightStatRow(
                    icon = Icons.Default.PieChart,
                    label = topCategoryLabel,
                    value = topCategoryText
                )

                Spacer(Modifier.height(10.dp))

                val savingsValue = if (insights.savedAmount > 0) {
                    if (selectedType == Type.INCOME && insights.savingsPercentage > 0) {
                        "${currencyMinimalFormatter(insights.savedAmount)} (${insights.savingsPercentage}%)"
                    } else {
                        currencyMinimalFormatter(insights.savedAmount)
                    }
                } else {
                    "-"
                }

                InsightStatRow(
                    icon = Icons.Default.Savings,
                    label = "Disisihkan ke Tabungan",
                    value = savingsValue
                )
            }
        }
    }
}

@Composable
private fun InsightStatRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
