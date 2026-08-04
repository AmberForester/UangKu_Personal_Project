package com.example.uangku.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uangku.feature.category.presentation.CategoryScreen
import com.example.uangku.feature.category.presentation.CategoryViewModel
import com.example.uangku.feature.transaction.presentation.TransactionFormScreen
import com.example.uangku.feature.transaction.presentation.TransactionScreen
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel


@Composable
fun UangKuNavGraph(
    navController: NavController,
    categoryViewModel: CategoryViewModel,
    transactionViewModel: TransactionViewModel,
    transactionFormViewModel: TransactionFormViewModel
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Category.route,
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

        }

        composable (Destination.Analysis.route) {

        }

        composable (Destination.Settings.route) {

        }
    }
}