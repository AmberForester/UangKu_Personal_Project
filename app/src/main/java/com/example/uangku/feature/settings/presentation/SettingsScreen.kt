package com.example.uangku.feature.settings.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.period.presentation.PeriodDialog
import com.example.uangku.feature.period.presentation.PeriodEvent
import com.example.uangku.feature.period.presentation.PeriodViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(

    navController: NavController,
    periodViewModel: PeriodViewModel

) {

    val periodState by periodViewModel.state.collectAsState()
    var showPeriodDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        periodViewModel.onEvent(PeriodEvent.onScreenOpen)
    }

    Scaffold (
        topBar = {
            TopAppBar(title = "Settings")
        },
        bottomBar = { UangKuNavigationBar(navController = navController, current = "settings") }

    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ){
            Spacer(Modifier.padding(20.dp))
            SettingsItem(
                title = "Financial Period",
                description = "",
                icon = Icons.Default.Timeline,
                onClick = { showPeriodDialog = true }
            )

            SettingsItem(
                title = "Category",
                description = "",
                icon = Icons.Default.List,
                onClick = { navController.navigate("category") }
            )
        }
        ShowPeriodDialog(
            showDialog = showPeriodDialog,
            selectedDate = periodState.selectedStartDay,
            onDismiss = { showPeriodDialog = false },
            onConfirm = { selected ->
                periodViewModel.onEvent(PeriodEvent.onStartDayChange(selected))
                periodViewModel.onEvent(PeriodEvent.onSave)
                showPeriodDialog = false
            }
        )
    }
}

@Composable
fun ShowPeriodDialog(

    showDialog: Boolean,
    selectedDate: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit

) {
    if(showDialog){
        PeriodDialog(
            selectedDate = selectedDate,
            onDismissDialog = onDismiss,
            onConfirmDialog = { selected ->
                onConfirm(selected)
            }
        ) 
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(35.dp)
                )
            }

            Spacer(Modifier.padding(5.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
        }
    }
}