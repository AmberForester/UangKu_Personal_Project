package com.example.uangku.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun UangKuNavigationBar(
    navController: NavController
) {

    val current by remember { mutableStateOf("") }
    Column {
        NavigationBar {
            Row {
                NavigationBarItem(
                    selected = current == "dashboard",
                    onClick = {
                        navController.navigate("dashboard")
                    },
                    label = {
                        Text(text = "Dashboard")
                    },
                    icon = {},
                )
                NavigationBarItem(
                    selected = current == "transaction",
                    onClick = {
                        navController.navigate("transaction")
                    },
                    label = {
                        Text(text = "Transaction")
                    },
                    icon = {},
                )
                NavigationBarItem(
                    selected = current == "budget",
                    onClick = {
                        navController.navigate("budget")
                    },
                    label = {
                        Text(text = "Budget")
                    },
                    icon = {},
                )
                NavigationBarItem(
                    selected = current == "analysis",
                    onClick = {
                        navController.navigate("analysis")
                    },
                    label = {
                        Text(text = "Analysis")
                    },
                    icon = {},
                )
//                NavigationBarItem(
//                    selected = current == "settings",
//                    onClick = {
//                        navController.navigate("settings")
//                    },
//                    label = {
//                        Text(text = "Settings")
//                    },
//                    icon = {},
//                )
                NavigationBarItem(
                    selected = current == "category",
                    onClick = {
                        navController.navigate("category")
                    },
                    label = {
                        Text(text = "Category (Temp)")
                    },
                    icon = {},
                )
            }
        }
    }
}


