package com.example.uangku.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UangKuNavigationBar(
    navController: NavController,
    current: String
) {

    Column {
        NavigationBar {
            NavigationBarItem(

                selected = current == "dashboard",
                onClick = {
                    navController.navigate("dashboard")
                },
                label = {
                    if(current.equals("dashboard", ignoreCase = true))
                    Text(text = "Dashboard", fontSize = 10.sp)
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "dashboard Icon",
                    )
                },

            )
            NavigationBarItem(
                selected = current == "transaction",
                onClick = {
                    navController.navigate("transaction")
                },
                label = {
                    if(current.equals("transaction", ignoreCase = true)){
                        Text(text = "Transaction", fontSize = 10.sp)
                    }
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "dashboard Icon",
                    )
                },
            )
            NavigationBarItem(
                selected = current == "budget",
                onClick = {
                    navController.navigate("budget")
                },
                label = {
                    if(current.equals("budget", ignoreCase = true))
                    Text(text = "Budget", fontSize = 10.sp)
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "dashboard Icon",
                    )
                },
            )
            NavigationBarItem(
                selected = current == "analysis",
                onClick = {
                    navController.navigate("analysis")
                },
                label = {
                    if(current.equals("analysis", ignoreCase = true))
                    Text(text = "Analysis", fontSize = 10.sp)
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "dashboard Icon",
                    )
                },
            )

            NavigationBarItem(
                selected = current == "settings",
                onClick = {
                    navController.navigate("settings")
                },
                alwaysShowLabel = true,
                label = {
                    if(current.equals("settings", ignoreCase = true))
                    Text(text = "Settings", fontSize = 10.sp)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "dashboard Icon",
                    )
                },
            )

        }
    }
}


