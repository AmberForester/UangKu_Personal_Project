package com.example.uangku.feature.budget.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.brandSkyBlue
import com.example.uangku.core.ui.theme.budgetBorderColor
import com.example.uangku.core.ui.theme.cardSkyBlueEnd
import com.example.uangku.core.ui.theme.cardSkyBlueStart
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.budget.domain.BudgetOverview

@Composable
fun BudgetOverviewItem(
    overview: BudgetOverview
) {
    fun formatWithSpace(amount: Double): String {
        val formatted = currencyMinimalFormatter(amount)
        return if (formatted.startsWith("Rp") && !formatted.startsWith("Rp ")) {
            formatted.replaceFirst("Rp", "Rp ")
        } else {
            formatted
        }
    }

    val allocationProgress = if (overview.income > 0) {
        (overview.allocated / overview.income).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }

    val allocationPercent = if (overview.income > 0) {
        ((overview.allocated / overview.income) * 100).toInt()
    } else {
        0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
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
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Bagian Atas: Biru Langit Gradien (Sisa Anggaran Tersedia & Mini Progress)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                cardSkyBlueStart,
                                cardSkyBlueEnd
                            )
                        )
                    )
            ) {
                // Aksen lengkungan cahaya di latar belakang kanan atas
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-20).dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = "Sisa Anggaran Tersedia",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp
                        ),
                        color = Color(0xFF334155)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = formatWithSpace(overview.available),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 24.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Progress bar alokasi dari pemasukan
                    LinearProgressIndicator(
                        progress = { allocationProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = brandSkyBlue,
                        trackColor = Color.White.copy(alpha = 0.5f)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Teralokasi $allocationPercent% dari total pemasukan",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.5.sp
                        ),
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bagian Bawah: Putih Bersih (Pemasukan Bulanan vs Total Teralokasi)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kolom Pemasukan
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Pemasukan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF64748B)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "+${formatWithSpace(overview.income)}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = successGreen
                    )
                }

                // Garis Pemisah Vertikal
                VerticalDivider(
                    modifier = Modifier
                        .height(36.dp)
                        .padding(horizontal = 12.dp),
                    color = Color(0xFFE2E8F0)
                )

                // Kolom Teralokasi
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Teralokasi",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF64748B)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = formatWithSpace(overview.allocated),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}
