package com.example.uangku.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uangku.feature.category.presentation.CategoryScreen
import com.example.uangku.feature.category.presentation.CategoryViewModel


@Composable
fun UangKuNavGraph(
    navController: NavController,
    categoryViewModel: CategoryViewModel
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

        }

        composable (Destination.Budget.route) {

        }

        composable (Destination.Analysis.route) {

        }

        composable (Destination.Settings.route) {

        }
    }
}