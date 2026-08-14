package com.example.uangku.feature.budget.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.uangku.feature.budget.presentation.component.BudgetDialog
import com.example.uangku.feature.budget.presentation.component.BudgetItem
import com.example.uangku.feature.budget.presentation.component.BudgetOverviewItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    navController: NavController
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            BudgetEvent.onScreenOpen
        )
    }

    Scaffold (

        topBar = {
            TopAppBar(
                title = "Budget"
            )
        },
        bottomBar = {
            UangKuNavigationBar(navController = navController)
        }

    ){ padding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                BudgetOverviewItem(overview = state.budgetOverview)
            }

            items(state.budgets){ budget ->

                Log.d("check category on screen", "categoryId: ${budget.categoryId}")

                BudgetItem(
                    budget = budget,
                    onEditClick = { viewModel.onEvent(BudgetEvent.onItemClick(budget)) }
                )
            }
        }
    }
    ShowDialog(viewModel)
}

@Composable
fun ShowDialog(
    viewModel: BudgetViewModel
) {
    val state = viewModel.state.collectAsState().value
    val selected = state.selectedBudget
    Log.d("check category on dialog", "categoryId: ${selected?.categoryId}")
    if(state.showDialog){
        BudgetDialog(
            categoryName = selected!!.categoryName,
            amount = state.amount,
            onAmountChange = {
                viewModel.onEvent(
                    BudgetEvent.onAmountChange(it)
                )
            },
            errorMessage = state.errorMessage,
            onDismiss = {
                viewModel.onEvent(
                    BudgetEvent.onDismissDialog
                )
            },
            availableAmount = state.availableAmount,
            onSave = {
                viewModel.onEvent(
                    BudgetEvent.onSaveClick
                )
            },
            onReset = {
                viewModel.onEvent(
                    BudgetEvent.onReset
                )
            }
        )
    }
}