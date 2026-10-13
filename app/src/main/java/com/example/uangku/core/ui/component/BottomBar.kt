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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.ui.theme.brandSkyBlue
import com.example.uangku.core.ui.theme.brandSkyBlueLight

@Composable
fun UangKuNavigationBar(
    navController: NavController,
    current: String
) {
    Column {
        HorizontalDivider(
            color = Color(0xFFE2E8F0),
            thickness = 0.8.dp
        )

        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            val itemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = brandSkyBlue,
                selectedTextColor = brandSkyBlue,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = brandSkyBlueLight.copy(alpha = 0.45f)
            )

            NavigationBarItem(
                selected = current.equals("dashboard", ignoreCase = true),
                onClick = { navController.navigate("dashboard") },
                colors = itemColors,
                label = {
                    if (current.equals("dashboard", ignoreCase = true)) {
                        Text(
                            text = "Dashboard",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Dashboard Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("transaction", ignoreCase = true),
                onClick = { navController.navigate("transaction") },
                colors = itemColors,
                label = {
                    if (current.equals("transaction", ignoreCase = true)) {
                        Text(
                            text = "Transaction",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Transaction Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("budget", ignoreCase = true),
                onClick = { navController.navigate("budget") },
                colors = itemColors,
                label = {
                    if (current.equals("budget", ignoreCase = true)) {
                        Text(
                            text = "Budget",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Budget Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("analysis", ignoreCase = true),
                onClick = { navController.navigate("analysis") },
                colors = itemColors,
                label = {
                    if (current.equals("analysis", ignoreCase = true)) {
                        Text(
                            text = "Analysis",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analysis Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = current.equals("settings", ignoreCase = true),
                onClick = { navController.navigate("settings") },
                colors = itemColors,
                label = {
                    if (current.equals("settings", ignoreCase = true)) {
                        Text(
                            text = "Settings",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon"
                    )
                }
            )
        }
    }
}
