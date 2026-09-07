package com.example.uangku.feature.transaction.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import com.example.uangku.feature.transaction.presentation.component.PeriodSelector
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

            Spacer(Modifier.padding(10.dp))

            ShowTransactionSummary(
                monthlySummary = state.monthlySummary,
                overallSummary = state.overallSummary
            )

            Spacer(Modifier.padding(20.dp))

            TransactionContent(
                state = state,
                onClick = { transaction ->
                    navController.navigate("transaction_form/${transaction.id}")
                },
            )
        }
    }
}






