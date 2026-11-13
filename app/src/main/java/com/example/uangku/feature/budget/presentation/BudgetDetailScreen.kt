package com.example.uangku.feature.budget.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.core.ui.theme.lightOrangeBg
import com.example.uangku.core.ui.theme.lightOrangeComponent
import com.example.uangku.core.ui.theme.orangeBg
import com.example.uangku.core.ui.theme.orangeComponent
import com.example.uangku.core.ui.theme.redBg
import com.example.uangku.core.ui.theme.redComponent
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.budget.domain.BudgetSummary
import com.example.uangku.feature.period.domain.FinancialPeriod
import com.example.uangku.feature.transaction.domain.Transaction
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetDetailScreen(
    categoryId: Int,
    viewModel: BudgetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTransaction: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.onEvent(BudgetEvent.OnDetailScreenOpen(categoryId))
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(BudgetEvent.OnDetailScreenClose)
        }
    }

    val budget = state.currentDetailBudget

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = budget?.categoryName ?: "Detail Anggaran",
                onBackClick = onNavigateBack,
                actions = {
                    if (budget != null) {
                        IconButton(onClick = { viewModel.onEvent(BudgetEvent.onItemClick(budget)) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Ubah Anggaran",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isDetailLoading && budget == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (budget != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kartu Hero Ringkasan Anggaran & Periode
                item {
                    DetailBudgetSummaryCard(
                        budget = budget,
                        period = state.currentPeriod,
                        onEditClick = { viewModel.onEvent(BudgetEvent.onItemClick(budget)) }
                    )
                }

                // Section Judul Transaksi Kategori
                item {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Transaksi Periode Ini",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${state.detailTransactions.size} Transaksi",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Daftar Transaksi Terkait (Lengkap dengan Tanggal)
                if (state.detailTransactions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada transaksi di kategori ini",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                state.detailTransactions.forEachIndexed { index, transaction ->
                                    BudgetDetailTransactionItem(
                                        transaction = transaction,
                                        onClick = {
                                            transaction.id?.let(onNavigateToTransaction)
                                        }
                                    )
                                    if (index < state.detailTransactions.lastIndex) {
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outline,
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 54.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Fallback Empty State jika data anggaran tidak ditemukan
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Data Anggaran Tidak Ditemukan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Kategori atau anggaran untuk periode ini tidak tersedia.",
                            fontSize = 12.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(18.dp))

                        OutlinedButton(
                            onClick = onNavigateBack,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Text(
                                text = "Kembali",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    ShowDialog(viewModel = viewModel)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DetailBudgetSummaryCard(
    budget: BudgetSummary,
    period: FinancialPeriod?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
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

    val animatedProgress by animateFloatAsState(targetValue = budget.progress, label = "budget_detail_progress")

    val periodFormatter = remember {
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
    }

    val periodRangeText = period?.let {
        "${it.startDate.format(periodFormatter)} - ${it.endDate.format(periodFormatter)}"
    } ?: "Periode Aktif"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Baris: Info Periode & Badge Persentase
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Periode",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = periodRangeText,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (budget.budgetAmount > 0.0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(boxBgColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${budget.percentage}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = progressColor
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // Baris Terpakai & Total Anggaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Pengeluaran",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = currencyMinimalFormatter(budget.spent),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBudgetMaxedOrOver) redComponent else MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Batas Anggaran",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = if (budget.budgetAmount > 0.0) currencyMinimalFormatter(budget.budgetAmount) else "Belum diatur",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (budget.budgetAmount == 0.0) {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Atur Anggaran Kategori Ini",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Spacer(Modifier.height(14.dp))

                // Linear Progress Indicator Halus
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = progressColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(Modifier.height(10.dp))

                // Baris Status Sisa atau Kelebihan Anggaran
                val remainingText = if (budget.isOverBudget) {
                    "Melebihi anggaran ${currencyMinimalFormatter(-budget.remaining)}"
                } else {
                    "Sisa anggaran ${currencyMinimalFormatter(budget.remaining)}"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = remainingText,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isBudgetMaxedOrOver) redComponent else GreenComponent
                    )

                    Text(
                        text = if (isBudgetMaxedOrOver) "Perlu evaluasi" else "Terkendali",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetDetailTransactionItem(
    transaction: Transaction,
    onClick: () -> Unit
) {
    val isIncome = transaction.type == Type.INCOME

    val cal = Calendar.getInstance().apply { time = transaction.date }
    val nowCal = Calendar.getInstance()
    val isToday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)
    nowCal.add(Calendar.DAY_OF_YEAR, -1)
    val isYesterday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    val dateStr = when {
        isToday -> "Hari ini"
        isYesterday -> "Kemarin"
        else -> SimpleDateFormat("d MMM yy", Locale("id", "ID")).format(transaction.date)
    }

    val title = if (transaction.description.isNotBlank()) transaction.description else transaction.categoryName
    val subtitle = if (transaction.description.isNotBlank()) "${transaction.categoryName} • $dateStr" else "${transaction.categoryName} • $dateStr"
    val amountFormatted = currencyMinimalFormatter(transaction.amount)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle Avatar
        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = if (isIncome) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (isIncome) successGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Judul & Subtitle (Lengkap dengan Tanggal & Nama Kategori)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nominal (+Rp / -Rp)
        Text(
            text = if (isIncome) "+$amountFormatted" else "-$amountFormatted",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            fontWeight = FontWeight.SemiBold,
            color = if (isIncome) successGreen else generalRed
        )
    }
}
