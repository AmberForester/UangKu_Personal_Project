package com.example.uangku.feature.dashboard.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.secondaryLightSkyBlue
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.transaction.domain.TransactionSummary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    summary: TransactionSummary,
    financialPeriod: FinancialPeriod? = null,
) {
    var isBalanceVisible by rememberSaveable { mutableStateOf(true) }

    val isDark = MaterialTheme.colorScheme.background != com.example.uangku.core.ui.theme.mainBgSkyBlue
    val gradientColors = if (isDark) {
        listOf(Color(0xFF0369A1), Color(0xFF0C4A6E))
    } else {
        listOf(
            mainSkyBlue.copy(0.7f),
            secondaryLightSkyBlue
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Kartu Utama
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
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
                // Bagian Atas: Biru Muda Gradien (Total Saldo & Toggle Mata)
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
                    // Efek aksen lengkungan cahaya di latar belakang kanan atas
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-10).dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = if (isDark) 0.1f else 0.25f))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Saldo",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp
                                ),
                                color = if (isDark) Color(0xFFBAE6FD) else Color(0xFF334155),
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = if (isBalanceVisible) {
                                    currencyFormatter(summary.balance)
                                } else {
                                    "••••••••"
                                },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 24.sp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color(0xFF0F172A)
                            )
                        }

                        IconButton(
                            onClick = { isBalanceVisible = !isBalanceVisible },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isBalanceVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isBalanceVisible) "Sembunyikan Saldo" else "Tampilkan Saldo",
                                tint = if (isDark) Color(0xFFBAE6FD) else Color(0xFF334155),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // Bagian Bawah: (Pemasukan Bulanan & Pengeluaran Bulanan)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Kolom Pemasukan Bulanan
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Pemasukan",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = if (isBalanceVisible) {
                                "+${currencyMinimalFormatter(summary.income)}"
                            } else {
                                "+••••"
                            },
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

                    // Kolom Pengeluaran Bulanan
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Pengeluaran",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = if (isBalanceVisible) {
                                "-${currencyMinimalFormatter(summary.expense)}"
                            } else {
                                "-••••"
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = generalRed
                        )
                    }
                }
            }
        }

        // Badge Periode Mengambang di Bibir Atas Kartu (Rata Kiri)
        if (financialPeriod != null) {
            PeriodBadge(
                financialPeriod = financialPeriod,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp)
                    .zIndex(1f)
            )
        }
    }
}
