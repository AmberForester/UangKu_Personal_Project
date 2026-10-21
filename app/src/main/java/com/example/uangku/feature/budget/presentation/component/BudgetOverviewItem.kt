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
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.core.ui.theme.lightSkyBlue
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.core.ui.theme.secondaryLightSkyBlue
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.budget.domain.BudgetOverview

@Composable
fun BudgetOverviewItem(
    overview: BudgetOverview
) {

    val allocationProgress = if (overview.income > 0) {
        (overview.allocated / overview.income).coerceIn(0.0, 1.0).toFloat()
    } else if (overview.allocated > 0) {
        1.0f
    }else {
        0f
    }

    val allocationPercent = if (overview.income > 0) {
        ((overview.allocated / overview.income) * 100).toInt()
    } else {
        0
    }

    val allocationText = when {
        overview.income == 0.0 && overview.allocated > 0 ->
            "Teralokasi ${currencyFormatter(overview.allocated)} (Belum ada pemasukan)"
        overview.allocated  > overview.income ->
            "Teralokasi $allocationPercent% dari total pemasukan (Melebihi pemasukan)"
        else ->
            "Teralokasi $allocationPercent% dari total pemasukan"
    }

    val isDark = MaterialTheme.colorScheme.background != com.example.uangku.core.ui.theme.mainBgSkyBlue
    val gradientColors = if (isDark) {
        listOf(Color(0xFF075985), Color(0xFF0C4A6E))
    } else {
        listOf(lightSkyBlue, secondaryLightSkyBlue)
    }

    Card(
        modifier = Modifier
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
            modifier = Modifier.fillMaxWidth()
        ) {
            // Bagian Atas: Biru Langit Gradien (Sisa Anggaran Tersedia & Mini Progress)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = gradientColors
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
                        .background(Color.White.copy(alpha = if (isDark) 0.1f else 0.25f))
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
                        color = if (isDark) Color(0xFFBAE6FD) else Color(0xFF334155)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = currencyFormatter(overview.available),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 24.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color(0xFF0F172A)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Progress bar alokasi dari pemasukan
                    LinearProgressIndicator(
                        progress = { allocationProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (overview.allocated > overview.income) {
                            redComponent
                        } else {
                            if (isDark) Color(0xFF38BDF8) else mainSkyBlue
                        },
                        trackColor = Color.White.copy(alpha = if (isDark) 0.2f else 0.5f)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = allocationText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.5.sp
                        ),
                        color = if (isDark) Color(0xFFBAE6FD) else Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bagian Bawah: (Pemasukan Bulanan vs Total Teralokasi)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "+${currencyFormatter(overview.income)}",
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
                    color = MaterialTheme.colorScheme.outline
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = currencyFormatter(overview.allocated),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
