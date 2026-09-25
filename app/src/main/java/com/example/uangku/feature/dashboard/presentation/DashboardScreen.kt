package com.example.uangku.feature.dashboard.presentation

import RecentTransactionSection
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.uangku.feature.dashboard.presentation.component.PeriodSection
import com.example.uangku.feature.dashboard.presentation.component.PreviewBudgetSection
import com.example.uangku.feature.dashboard.presentation.component.SummaryCard

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
        topBar = {
            TopAppBar(
                title = "Dashboard"
            )
        },
        bottomBar = {
            UangKuNavigationBar(
                navController = navController,
                current = "dashboard"
            )
        }
    ) {  padding ->

        Column (
            modifier = Modifier.padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            PeriodSection(
                financialPeriod = state.financialPeriod
            )

            Spacer(Modifier.height(10.dp))

            SummaryCard(
                summary = state.summary
            )

            Spacer(Modifier.height(10.dp))

            PreviewBudgetSection(
                budgets = state.budgets,
                onSeeAllClick = { navController.navigate("budget") }
            )

            RecentTransactionSection(
                transactions = state.recentTransactions,
                onSeeAllClick = { navController.navigate("transaction")}
            )
        }
    }
}