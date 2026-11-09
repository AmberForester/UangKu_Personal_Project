package com.example.uangku.core.navigation

sealed class Destination (
    val route: String
){

    data object Category : Destination("category")

    data object Dashboard : Destination("dashboard")

    data object Transaction : Destination("transaction")

    data object Budget : Destination("budget")

    data object Analysis : Destination("analysis")

    data object Settings : Destination("settings")

    data object TransactionForm : Destination("transaction_form/{id}")

    data object Savings : Destination("savings")

    data object SavingsDetail : Destination("savings_detail/{goalId}") {
        fun createRoute(goalId: Long): String = "savings_detail/$goalId"
    }


}