package com.example.uangku.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uangku.feature.budget.presentation.BudgetScreen
import com.example.uangku.feature.budget.presentation.BudgetViewModel
import com.example.uangku.feature.category.presentation.CategoryScreen
import com.example.uangku.feature.category.presentation.CategoryViewModel
import com.example.uangku.feature.period.presentation.PeriodViewModel
import com.example.uangku.feature.settings.presentation.SettingsScreen
import com.example.uangku.feature.transaction.presentation.TransactionFormScreen
import com.example.uangku.feature.transaction.presentation.TransactionScreen
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UangKuNavGraph(

    navController: NavController,
    categoryViewModel: CategoryViewModel,
    transactionViewModel: TransactionViewModel,
    transactionFormViewModel: TransactionFormViewModel,
    budgetViewModel: BudgetViewModel,
    periodViewModel: PeriodViewModel
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Transaction.route,
    ) {
        composable(Destination.Category.route) {
            CategoryScreen(
                viewModel = categoryViewModel,
                navController = navController
            )
        }

        composable (Destination.Dashboard.route) {

        }

        composable (Destination.Transaction.route) {
            TransactionScreen(
                viewModel = transactionViewModel,
                navController = navController,
            )
        }

        composable(
            route = Destination.TransactionForm.route,
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("id")

            TransactionFormScreen(
                id = id,
                viewModel = transactionFormViewModel,
                navController = navController
            )
        }

        composable (Destination.Budget.route) {
           BudgetScreen(
               viewModel = budgetViewModel,
               navController = navController
           )
        }

        composable (Destination.Analysis.route) {

        }

        composable (Destination.Settings.route) {
            SettingsScreen(
                navController = navController,
                periodViewModel = periodViewModel
            )
        }
    }
}