package com.example.uangku.feature.analysis.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.analysis.presentation.component.CategoryTrendLineChart
import com.example.uangku.feature.transaction.domain.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryAnalysisScreen(
    categoryId: Int,
    viewModel: AnalysisViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTransaction: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.onEvent(AnalysisEvent.OnLoadCategoryDetail(categoryId))
    }

    val detail = state.categoryDetail
    val categoryName = detail?.categoryName ?: "Analisis Kategori"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = categoryName,
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        if (detail == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val isExpense = detail.type == Type.EXPENSE
            val themeColor = if (isExpense) MaterialTheme.colorScheme.primary else successGreen

            val sortedTransactions = remember(detail.transactions, state.categoryDetailSortOrder) {
                when (state.categoryDetailSortOrder) {
                    CategoryDetailSortOrder.NEWEST -> detail.transactions.sortedWith(
                        compareByDescending<Transaction> { it.date }.thenByDescending { it.id ?: 0L }
                    )
                    CategoryDetailSortOrder.HIGHEST_AMOUNT -> detail.transactions.sortedWith(
                        compareByDescending<Transaction> { it.amount }.thenByDescending { it.date }
                    )
                    CategoryDetailSortOrder.LOWEST_AMOUNT -> detail.transactions.sortedWith(
                        compareBy<Transaction> { it.amount }.thenByDescending { it.date }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Kartu Ringkasan Bulan Ini & Status Kenaikan vs Rata-rata
                item {
                    CategoryOverviewCard(
                        detail = detail,
                        themeColor = themeColor,
                        historyMonths = state.categoryDetailHistoryMonths
                    )
                }

                // 2. Diagram Garis Tren 3 / 6 / 12 Bulan Terakhir
                item {
                    CategoryTrendLineChart(
                        monthlyHistory = detail.monthlyHistory,
                        averageAmount = detail.averageMonthlyAmount,
                        lineColor = themeColor,
                        selectedMonth = detail.targetMonth,
                        selectedHistoryMonths = state.categoryDetailHistoryMonths,
                        isLoading = state.isLoadingCategoryDetail,
                        onRangeSelected = { months ->
                            viewModel.onEvent(AnalysisEvent.OnChangeCategoryDetailHistoryMonths(months))
                        }
                    )
                }

                // 3. Tiga Kartu Metrik Cepat (Rata-rata, Puncak Pengeluaran, Frekuensi)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickMetricCard(
                            title = "Rata-rata",
                            value = currencyMinimalFormatter(detail.averageMonthlyAmount),
                            icon = Icons.Default.CalendarMonth,
                            modifier = Modifier.weight(1f)
                        )
                        QuickMetricCard(
                            title = "Terbesar",
                            value = currencyMinimalFormatter(detail.peakTransaction?.amount ?: 0.0),
                            icon = Icons.Default.FlashOn,
                            modifier = Modifier.weight(1f)
                        )
                        QuickMetricCard(
                            title = "Frekuensi",
                            value = "${detail.transactionCount} kali",
                            icon = Icons.AutoMirrored.Filled.ReceiptLong,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 4. Header Daftar Transaksi Periode Ini & Pilihan Pengurutan
                item {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Transaksi Periode Ini",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "${detail.transactions.size} transaksi",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (detail.transactions.isNotEmpty()) {
                            CategorySortDropdown(
                                currentSortOrder = state.categoryDetailSortOrder,
                                onSortOrderSelected = { order ->
                                    viewModel.onEvent(AnalysisEvent.OnChangeCategoryDetailSortOrder(order))
                                }
                            )
                        }
                    }
                }

                // 5. Daftar Transaksi Periode Ini (Satu Wadah Kartu Besar dengan Pemisah)
                if (sortedTransactions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada transaksi di kategori ini pada periode ini",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
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
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 2.dp)
                            ) {
                                sortedTransactions.forEachIndexed { index, tx ->
                                    CategoryTransactionItem(
                                        transaction = tx,
                                        isExpense = isExpense,
                                        onClick = { tx.id?.let { onNavigateToTransaction(it) } }
                                    )
                                    if (index < sortedTransactions.lastIndex) {
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 50.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySortDropdown(
    currentSortOrder: CategoryDetailSortOrder,
    onSortOrderSelected: (CategoryDetailSortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val currentLabel = when (currentSortOrder) {
        CategoryDetailSortOrder.NEWEST -> "Terbaru"
        CategoryDetailSortOrder.HIGHEST_AMOUNT -> "Terbesar"
        CategoryDetailSortOrder.LOWEST_AMOUNT -> "Terkecil"
    }

    Box(modifier = modifier) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = currentLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(14.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shadowElevation = 6.dp
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Terbaru",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = if (currentSortOrder == CategoryDetailSortOrder.NEWEST) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (currentSortOrder == CategoryDetailSortOrder.NEWEST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                trailingIcon = if (currentSortOrder == CategoryDetailSortOrder.NEWEST) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                onClick = {
                    onSortOrderSelected(CategoryDetailSortOrder.NEWEST)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Nominal Terbesar",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = if (currentSortOrder == CategoryDetailSortOrder.HIGHEST_AMOUNT) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (currentSortOrder == CategoryDetailSortOrder.HIGHEST_AMOUNT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                trailingIcon = if (currentSortOrder == CategoryDetailSortOrder.HIGHEST_AMOUNT) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                onClick = {
                    onSortOrderSelected(CategoryDetailSortOrder.HIGHEST_AMOUNT)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Nominal Terkecil",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = if (currentSortOrder == CategoryDetailSortOrder.LOWEST_AMOUNT) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (currentSortOrder == CategoryDetailSortOrder.LOWEST_AMOUNT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                trailingIcon = if (currentSortOrder == CategoryDetailSortOrder.LOWEST_AMOUNT) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                onClick = {
                    onSortOrderSelected(CategoryDetailSortOrder.LOWEST_AMOUNT)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun CategoryOverviewCard(
    detail: com.example.uangku.feature.analysis.domain.CategoryAnalysisDetail,
    themeColor: Color,
    historyMonths: Int,
    modifier: Modifier = Modifier
) {
    val isExpense = detail.type == Type.EXPENSE

    // Menentukan status perbandingan vs rata-rata historis
    val isHigher = detail.isHigherThanAverage
    val pct = detail.percentageVsAverage
    val rangeLabel = if (historyMonths == 12) "1 tahun" else "$historyMonths bulan"

    val (badgeText, badgeColor, badgeIcon) = when {
        detail.averageMonthlyAmount <= 0 -> Triple(
            "Belum ada data riwayat di bulan sebelumnya",
            MaterialTheme.colorScheme.primary,
            Icons.Default.Info
        )
        pct == 0 -> Triple(
            "Sesuai dengan rata-rata $rangeLabel",
            MaterialTheme.colorScheme.primary,
            Icons.Default.Info
        )
        isHigher -> {
            if (isExpense) {
                Triple(
                    "$pct% lebih tinggi dari rata-rata $rangeLabel",
                    generalRed,
                    Icons.AutoMirrored.Filled.TrendingUp
                )
            } else {
                Triple(
                    "$pct% lebih tinggi dari rata-rata $rangeLabel",
                    successGreen,
                    Icons.AutoMirrored.Filled.TrendingUp
                )
            }
        }
        else -> {
            if (isExpense) {
                Triple(
                    "$pct% lebih hemat dari rata-rata $rangeLabel",
                    successGreen,
                    Icons.AutoMirrored.Filled.TrendingDown
                )
            } else {
                Triple(
                    "$pct% lebih rendah dari rata-rata $rangeLabel",
                    generalRed,
                    Icons.AutoMirrored.Filled.TrendingDown
                )
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpense) "Pengeluaran Bulan Ini" else "Pemasukan Bulan Ini",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = themeColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (isExpense) "Pengeluaran" else "Pemasukan",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.Bold,
                        color = themeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = currencyMinimalFormatter(detail.currentMonthAmount),
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(12.dp))

            // Pill Indikator Tren vs Rata-rata
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = badgeColor.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CategoryTransactionItem(
    transaction: Transaction,
    isExpense: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        else -> SimpleDateFormat("d MMM yyyy", Locale("id", "ID")).format(transaction.date)
    }

    val title = if (transaction.description.isNotBlank()) transaction.description else transaction.categoryName
    val amountFormatted = currencyMinimalFormatter(transaction.amount)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = if (!isExpense) successGreen.copy(alpha = 0.14f) else generalRed.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (!isExpense) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (!isExpense) successGreen else generalRed,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${if (isExpense) "- " else "+ "}$amountFormatted",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            fontWeight = FontWeight.Bold,
            color = if (!isExpense) successGreen else MaterialTheme.colorScheme.onSurface
        )
    }
}
