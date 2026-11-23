package com.example.uangku.feature.analysis.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.domain.Type
import com.example.uangku.core.navigation.Destination
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.analysis.presentation.component.AnalysisCategoryItem
import com.example.uangku.feature.analysis.presentation.component.AnalysisDonutChart
import com.example.uangku.feature.analysis.presentation.component.AnalysisInsightCard
import com.example.uangku.feature.analysis.presentation.component.getCategoryColor
import com.example.uangku.feature.transaction.presentation.component.FormTypeSelector
import com.example.uangku.feature.transaction.presentation.component.PeriodSelector

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = "Analisis"
            )
        },
        bottomBar = {
            UangKuNavigationBar(
                navController = navController,
                current = "analysis"
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                // 1. Pemilih Periode Bulanan
                state.financialPeriod?.let { period ->
                    PeriodSelector(
                        financialPeriod = period,
                        onPreviousClick = { viewModel.onEvent(AnalysisEvent.OnPreviousMonth) },
                        onNextClick = { viewModel.onEvent(AnalysisEvent.OnNextMonth) }
                    )
                }
            }

            // 2. Toggle Pengeluaran vs Pemasukan
            item {
                FormTypeSelector(
                    selectedType = state.selectedType,
                    onTypeSelected = { viewModel.onEvent(AnalysisEvent.OnSelectType(it)) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // 3. Kartu Insight Finansial (Perbandingan Bulan Lalu & Fakta Menarik)
            item {
                AnalysisInsightCard(
                    insights = state.overview.insights,
                    hasTransactions = state.overview.totalAmount > 0 || state.overview.insights.savedAmount > 0,
                    selectedType = state.selectedType,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // 4. Donut Chart Komposisi Pengeluaran / Pemasukan
            item {
                AnalysisDonutChart(
                    totalAmount = state.overview.totalAmount,
                    categorySpendings = state.overview.categorySpendings,
                    selectedCategory = state.selectedCategory,
                    onCategoryClick = { category ->
                        viewModel.onEvent(AnalysisEvent.OnSelectCategory(category))
                    },
                    selectedType = state.selectedType,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // 5. Header Daftar Kategori
            if (state.overview.categorySpendings.isNotEmpty()) {
                item {
                    val categoryHeader = if (state.selectedType == Type.EXPENSE) {
                        "Kategori Pengeluaran"
                    } else {
                        "Kategori Pemasukan"
                    }
                    Text(
                        text = categoryHeader,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 22.dp, top = 4.dp, bottom = 2.dp)
                    )
                }

                // 6. Daftar Lengkap Kategori (Terbesar ke Terkecil)
                itemsIndexed(
                    items = state.overview.categorySpendings,
                    key = { _, item -> item.categoryId }
                ) { index, item ->
                    AnalysisCategoryItem(
                        item = item,
                        rank = index + 1,
                        color = getCategoryColor(index),
                        isSelected = state.selectedCategory?.categoryId == item.categoryId,
                        onClick = {
                            viewModel.onEvent(AnalysisEvent.OnSelectCategory(item))
                        },
                        onDetailClick = {
                            navController.navigate(Destination.CategoryAnalysis.createRoute(item.categoryId))
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            } else {
                item {
                    val typeLabel = if (state.selectedType == Type.EXPENSE) "pengeluaran" else "pemasukan"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = "Belum Ada Transaksi",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.5.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Belum ada catatan transaksi $typeLabel pada periode ini. Mulai catat keuanganmu agar analitik dapat ditampilkan.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
