package com.example.uangku.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uangku.feature.analysis.presentation.AnalysisScreen
import com.example.uangku.feature.google_drive.data.GoogleDriveAuth
import com.example.uangku.feature.backup.presentation.BackupViewModel
import com.example.uangku.feature.budget.presentation.BudgetDetailScreen
import com.example.uangku.feature.budget.presentation.BudgetScreen
import com.example.uangku.feature.budget.presentation.BudgetViewModel
import com.example.uangku.feature.category.presentation.CategoryScreen
import com.example.uangku.feature.category.presentation.CategoryViewModel
import com.example.uangku.feature.dashboard.presentation.DashboardScreen
import com.example.uangku.feature.dashboard.presentation.DashboardViewModel
import com.example.uangku.feature.export_excel.presentation.ExportExcelViewModel
import com.example.uangku.feature.period.presentation.PeriodViewModel
import com.example.uangku.feature.savings.presentation.SavingsDetailScreen
import com.example.uangku.feature.savings.presentation.SavingsScreen
import com.example.uangku.feature.savings.presentation.SavingsViewModel
import com.example.uangku.feature.settings.presentation.SettingsScreen
import com.example.uangku.feature.settings.presentation.SettingsViewModel
import com.example.uangku.feature.transaction.presentation.TransactionFormScreen
import com.example.uangku.feature.transaction.presentation.TransactionScreen
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UangKuNavGraph(

    categoryViewModel: CategoryViewModel,
    transactionViewModel: TransactionViewModel,
    transactionFormViewModel: TransactionFormViewModel,
    budgetViewModel: BudgetViewModel,
    periodViewModel: PeriodViewModel,
    dashboardViewModel: DashboardViewModel,
    backupViewModel: BackupViewModel,
    googleDriveAuth: GoogleDriveAuth,
    settingsViewModel: SettingsViewModel,
    exportExcelViewModel: ExportExcelViewModel,
    savingsViewModel: SavingsViewModel
){
    val navController = rememberNavController()

    NavHost(

        navController = navController,
        startDestination = Destination.Dashboard.route,

    ) {
        composable(Destination.Category.route) {
            CategoryScreen(
                viewModel = categoryViewModel,
                navController = navController
            )
        }

        composable (Destination.Dashboard.route) {
            DashboardScreen(
                navController = navController,
                dashboardViewModel = dashboardViewModel
            )
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
            val id = backStackEntry.arguments?.getLong("id") ?: -1L

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

        composable(
            route = Destination.BudgetDetail.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: -1
            BudgetDetailScreen(
                categoryId = categoryId,
                viewModel = budgetViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransaction = { txId ->
                    navController.navigate("transaction_form/$txId")
                }
            )
        }

        composable (Destination.Analysis.route) {
            AnalysisScreen(
                navController = navController
            )
        }

        composable (Destination.Settings.route) {
            SettingsScreen(
                navController = navController,
                periodViewModel = periodViewModel,
                backupViewModel = backupViewModel,
                googleDriveAuth = googleDriveAuth,
                settingsViewModel = settingsViewModel,
                exportExcelViewModel = exportExcelViewModel
            )
        }

        composable(Destination.Savings.route) {
            SavingsScreen(
                viewModel = savingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { goalId ->
                    navController.navigate(Destination.SavingsDetail.createRoute(goalId))
                }
            )
        }

        composable(
            route = Destination.SavingsDetail.route,
            arguments = listOf(
                navArgument("goalId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: -1L
            SavingsDetailScreen(
                goalId = goalId,
                viewModel = savingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
