package com.example.uangku

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uangku.core.navigation.UangKuNavGraph
import com.example.uangku.core.ui.theme.UangKuTheme
import com.example.uangku.feature.backup.presentation.BackupViewModel
import com.example.uangku.feature.backup.presentation.BackupViewModelFactory
import com.example.uangku.feature.budget.presentation.BudgetViewModel
import com.example.uangku.feature.budget.presentation.BudgetViewModelFactory
import com.example.uangku.feature.category.presentation.CategoryViewModel
import com.example.uangku.feature.category.presentation.CategoryViewModelFactory
import com.example.uangku.feature.dashboard.presentation.DashboardViewModel
import com.example.uangku.feature.dashboard.presentation.DashboardViewModelFactory
import com.example.uangku.feature.period.presentation.PeriodViewModel
import com.example.uangku.feature.period.presentation.PeriodViewModelFactory
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModelFactory
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModel
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer =(application as App).appContainer

        setContent {
            UangKuTheme {
                val categoryViewModel: CategoryViewModel = viewModel(
                    factory = CategoryViewModelFactory(
                        appContainer.categoryUseCase
                    )
                )
                val transactionViewModel: TransactionViewModel = viewModel(
                    factory = TransactionViewModelFactory(
                        appContainer.transactionUseCase
                    )
                )
                val transactionFormViewModel: TransactionFormViewModel = viewModel(
                    factory = TransactionFormViewModelFactory(
                        transactionUseCase = appContainer.transactionUseCase,
                        categoryUseCase = appContainer.categoryUseCase
                    )
                )
                val budgetViewModel: BudgetViewModel = viewModel(
                    factory = BudgetViewModelFactory(
                        budgetUseCase = appContainer.budgetUseCase
                    )
                )
                val periodViewModel: PeriodViewModel = viewModel(
                    factory = PeriodViewModelFactory(
                        periodUseCase = appContainer.periodUseCase
                    )
                )

                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(
                        periodUseCase = appContainer.periodUseCase,
                        transactionUseCase = appContainer.transactionUseCase,
                        budgetUseCase = appContainer.budgetUseCase
                    )
                )

                val backupViewModel: BackupViewModel = viewModel(
                    factory = BackupViewModelFactory(
                        backupUseCase = appContainer.backupUseCase,
                        fileManager = appContainer.backupFileManager,
                        jsonSerializer = appContainer.jsonSerializer
                    )
                )

                UangKuNavGraph(
                    categoryViewModel = categoryViewModel,
                    transactionViewModel = transactionViewModel,
                    transactionFormViewModel = transactionFormViewModel,
                    budgetViewModel = budgetViewModel,
                    periodViewModel = periodViewModel,
                    dashboardViewModel = dashboardViewModel,
                    backupViewModel = backupViewModel
                )
            }
        }
    }
}