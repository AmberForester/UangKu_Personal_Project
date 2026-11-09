package com.example.uangku.feature.savings.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.feature.savings.presentation.component.DeleteSavingGoalDialog
import com.example.uangku.feature.savings.presentation.component.SavingsFormDialog
import com.example.uangku.feature.savings.presentation.component.SavingsGoalItem
import com.example.uangku.feature.savings.presentation.component.SavingsHeader

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavingsScreen(
    viewModel: SavingsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(SavingsEvent.OnScreenOpen)
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(SavingsEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = "Target Tabungan",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(SavingsEvent.OnAddGoalClick) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Target Tabungan",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Total Tabungan
            item {
                SavingsHeader(
                    totalSavings = state.totalSavings,
                    activeGoalsCount = state.goals.size
                )
            }

            // Subtitle Section
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Daftar Pos Tabungan",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            if (state.goals.isEmpty()) {
                item {
                    SavingsEmptyState()
                }
            } else {
                items(
                    items = state.goals,
                    key = { it.id ?: it.title }
                ) { goal ->
                    SavingsGoalItem(
                        goal = goal,
                        onClick = { goal.id?.let(onNavigateToDetail) }
                    )
                }
            }
        }
    }

    // Dialog Tambah / Edit Target
    if (state.showGoalDialog) {
        SavingsFormDialog(
            isEdit = state.selectedGoal != null,
            title = state.titleInput,
            targetAmount = state.targetAmountInput,
            targetDate = state.targetDateInput,
            onTitleChange = { viewModel.onEvent(SavingsEvent.OnTitleChange(it)) },
            onTargetAmountChange = { viewModel.onEvent(SavingsEvent.OnTargetAmountChange(it)) },
            onTargetDateChange = { viewModel.onEvent(SavingsEvent.OnTargetDateChange(it)) },
            onDismiss = { viewModel.onEvent(SavingsEvent.OnDismissGoalDialog) },
            onConfirm = { viewModel.onEvent(SavingsEvent.OnSaveGoal) }
        )
    }

    // Dialog Konfirmasi Hapus Target
    if (state.showDeleteDialog && state.goalToDelete != null) {
        DeleteSavingGoalDialog(
            goalTitle = state.goalToDelete!!.title,
            onDismiss = { viewModel.onEvent(SavingsEvent.OnDismissDeleteDialog) },
            onConfirm = { viewModel.onEvent(SavingsEvent.OnConfirmDeleteGoal) }
        )
    }
}

@Composable
fun SavingsEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Savings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Belum Ada Pos Tabungan",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Mulai rencanakan pos tabungan untuk impian, liburan, atau dana daruratmu dengan mengetuk tombol + di bawah.",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
