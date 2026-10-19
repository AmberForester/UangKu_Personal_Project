package com.example.uangku.feature.transaction.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.transaction.presentation.component.AddTransactionButton
import com.example.uangku.feature.transaction.presentation.component.CategoryFilterBottomSheet
import com.example.uangku.feature.transaction.presentation.component.PeriodSelector
import com.example.uangku.feature.transaction.presentation.component.ShowTransactionSummary
import com.example.uangku.feature.transaction.presentation.component.TransactionContent
import com.example.uangku.feature.transaction.presentation.component.TransactionSearchBar
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) { viewModel.onEvent(TransactionEvent.onScreenOpen) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(title = "Transaksi")
        },
        floatingActionButton = {
            AddTransactionButton(
                onClick = {
                    viewModel.onEvent(TransactionEvent.onAddClick)
                    navController.navigate("transaction_form/-1")
                }
            )
        },
        bottomBar = {
            UangKuNavigationBar(navController = navController, current = "transaction")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(Modifier.height(8.dp))

            // 1. Bar Pencarian dan Tombol Filter Kategori (Berdampingan)
            TransactionSearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(TransactionEvent.onSearchQueryChange(it))
                },
                onClearSearch = {
                    viewModel.onEvent(TransactionEvent.onClearSearch)
                },
                selectedCategory = state.selectedCategory,
                onOpenFilter = {
                    viewModel.onEvent(TransactionEvent.onOpenFilterSheet)
                },
                onClearCategoryFilter = {
                    viewModel.onEvent(TransactionEvent.onCategoryFilterSelect(null))
                }
            )

            Spacer(Modifier.height(10.dp))

            // 2. Pemilih Periode Bulanan
            state.financialPeriod?.let {
                PeriodSelector(
                    onPreviousClick = {
                        viewModel.onEvent(
                            TransactionEvent.onPreviousMonth
                        )
                    },
                    onNextClick = {
                        viewModel.onEvent(
                            TransactionEvent.onNextMonth
                        )
                    },
                    financialPeriod = it
                )
            }

            Spacer(Modifier.height(14.dp))

            // 3. Ringkasan Keuangan Bulanan (Tetap utuh, tidak terfilter)
            ShowTransactionSummary(
                monthlySummary = state.monthlySummary,
                overallSummary = state.overallSummary
            )

            Spacer(Modifier.height(16.dp))

            // 4. Daftar Transaksi (Mengikuti Filter & Pencarian)
            TransactionContent(
                state = state,
                onResetFilter = {
                    viewModel.onEvent(TransactionEvent.onResetFilter)
                },
                onClick = { transaction ->
                    navController.navigate("transaction_form/${transaction.id}")
                },
            )
        }

        // 5. Modal Bottom Sheet Filter Kategori
        if (state.isFilterSheetVisible) {
            CategoryFilterBottomSheet(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onSelectCategory = {
                    viewModel.onEvent(TransactionEvent.onCategoryFilterSelect(it))
                },
                onDismiss = {
                    viewModel.onEvent(TransactionEvent.onDismissFilterSheet)
                }
            )
        }
    }
}
