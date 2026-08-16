package com.example.uangku.feature.transaction.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.uangku.core.navigation.Destination
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.transaction.domain.TransactionSummary
import com.example.uangku.feature.transaction.presentation.component.AddTransactionButton
import com.example.uangku.feature.transaction.presentation.component.MonthSelector
import com.example.uangku.feature.transaction.presentation.component.ShowTransactionSummary
import com.example.uangku.feature.transaction.presentation.component.TransactionContent
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
        topBar = {
            TopAppBar(title = "Transaction")
        },
        floatingActionButton = {
            AddTransactionButton(
                onClick = {
                    TransactionEvent.onAddClick
                    navController.navigate("transaction_form/-1")
                }
            )
        },
        bottomBar = {
            UangKuNavigationBar(navController = navController, current = "transaction")
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            MonthSelector(
                selectedMonth = state.selectedMonth,
                onPreviousClick = {
                    viewModel.onEvent(
                        TransactionEvent.onPreviousMonth
                    )
                },
                onNextClick = {
                    viewModel.onEvent(
                        TransactionEvent.onNextMonth
                    )
                }
            )

            ShowTransactionSummary(
                monthlySummary = state.monthlySummary,
                overallSummary = state.overallSummary
            )

            TransactionContent(
                state = state,
                onClick = { transaction ->
                    navController.navigate("transaction_form/${transaction.id}")
                },
            )
        }
    }
}






