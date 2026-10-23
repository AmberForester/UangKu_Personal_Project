package com.example.uangku.feature.dashboard.presentation

import com.example.uangku.feature.dashboard.presentation.component.RecentTransactionSection
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.uangku.feature.dashboard.presentation.component.PreviewBudgetSection
import com.example.uangku.feature.dashboard.presentation.component.SummaryCard
import com.example.uangku.feature.transaction.presentation.component.AddTransactionButton

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel
) {
    val state by dashboardViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.onEvent(DashboardEvent.onScreenOpen)
    }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = "Beranda"
            )
        },
        bottomBar = {
            UangKuNavigationBar(
                navController = navController,
                current = "dashboard"
            )
        },
        floatingActionButton = {
            AddTransactionButton(
                onClick = { navController.navigate("transaction_form/-1") }
            )
        }
    ) {  padding ->

        Column (
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(4.dp))

            SummaryCard(
                summary = state.summary,
                financialPeriod = state.financialPeriod
            )

            Spacer(Modifier.height(14.dp))

            PreviewBudgetSection(
                budgets = state.budgets,
                onSeeAllClick = { navController.navigate("budget") }
            )

            Spacer(Modifier.height(16.dp))

            RecentTransactionSection(
                transactions = state.recentTransactions,
                onSeeAllClick = { navController.navigate("transaction")}
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}