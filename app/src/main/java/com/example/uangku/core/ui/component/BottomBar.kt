package com.example.uangku.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun UangKuNavigationBar(
    navController: NavController,
    current: String
) {
    val navigateToTab: (String) -> Unit = { route ->
        if (!current.equals(route, ignoreCase = true)) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Column {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 0.8.dp
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            val itemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
            )

            NavigationBarItem(
                selected = current.equals("dashboard", ignoreCase = true),
                onClick = { navigateToTab("dashboard") },
                colors = itemColors,
                label = {
                    if (current.equals("dashboard", ignoreCase = true)) {
                        Text(
                            text = "Beranda",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Beranda"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("transaction", ignoreCase = true),
                onClick = { navigateToTab("transaction") },
                colors = itemColors,
                label = {
                    if (current.equals("transaction", ignoreCase = true)) {
                        Text(
                            text = "Transaksi",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Transaksi"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("budget", ignoreCase = true),
                onClick = { navigateToTab("budget") },
                colors = itemColors,
                label = {
                    if (current.equals("budget", ignoreCase = true)) {
                        Text(
                            text = "Anggaran",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Anggaran"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("analysis", ignoreCase = true),
                onClick = { navigateToTab("analysis") },
                colors = itemColors,
                label = {
                    if (current.equals("analysis", ignoreCase = true)) {
                        Text(
                            text = "Analisis",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analisis"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("settings", ignoreCase = true),
                onClick = { navigateToTab("settings") },
                colors = itemColors,
                label = {
                    if (current.equals("settings", ignoreCase = true)) {
                        Text(
                            text = "Pengaturan",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Pengaturan"
                    )
                }
            )
        }
    }
}
