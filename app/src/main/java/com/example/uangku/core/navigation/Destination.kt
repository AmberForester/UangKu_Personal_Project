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


}