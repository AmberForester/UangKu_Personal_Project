package com.example.uangku.feature.budget.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.uangku.core.ui.theme.lightSkyBlue
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.core.ui.theme.secondaryLightSkyBlue
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.budget.domain.BudgetOverview

@Composable
fun BudgetOverviewItem(
    overview: BudgetOverview,
    onNavigateToSavings: (() -> Unit)? = null
) {
    val availableToBudget = (overview.income - overview.saved).coerceAtLeast(0.0)

    val allocationProgress = if (overview.income == 0.0) {
        0f
    } else if (availableToBudget > 0) {
        (overview.allocated / availableToBudget).coerceIn(0.0, 1.0).toFloat()
    } else if (overview.allocated > 0) {
        1.0f
    } else {
        0f
    }

    val allocationPercent = if (availableToBudget > 0) {
        ((overview.allocated / availableToBudget) * 100).toInt()
    } else {
        0
    }

    val allocationText = when {
        overview.income == 0.0 && overview.allocated > 0 ->
            "Rencana anggaran disiapkan (Belum ada pemasukan)"
        availableToBudget == 0.0 && overview.allocated > 0 ->
            "Teralokasi ${currencyMinimalFormatter(overview.allocated)} (Melebihi dana tersedia)"
        overview.allocated > availableToBudget ->
            "Teralokasi $allocationPercent% dari dana tersedia (Melebihi dana tersedia)"
        overview.saved > 0 ->
            "Teralokasi $allocationPercent% dari total dana diluar tabungan"
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
                        text = currencyMinimalFormatter(overview.available),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 24.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color(0xFF0F172A)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Progress bar alokasi dari dana yang tersedia untuk budget
                    LinearProgressIndicator(
                        progress = { allocationProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (overview.income > 0.0 && overview.allocated > availableToBudget) {
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
                        text = "+${currencyMinimalFormatter(overview.income)}",
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
                        text = currencyMinimalFormatter(overview.allocated),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Baris Khusus Tabungan (hanya tampil jika ada dana yang disetor ke tabungan di periode ini)
            if (overview.saved > 0) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .then(
                            if (onNavigateToSavings != null) {
                                Modifier.clickable { onNavigateToSavings() }
                            } else Modifier
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            val savedPercent = if (overview.income > 0) {
                                " (${((overview.saved / overview.income) * 100).toInt()}%)"
                            } else ""

                            Text(
                                text = "Disisihkan ke Tabungan$savedPercent:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = currencyMinimalFormatter(overview.saved),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (onNavigateToSavings != null) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Lihat Tabungan",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
