package com.example.uangku.feature.savings.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingType
import com.example.uangku.feature.savings.presentation.component.DeleteSavingGoalDialog
import com.example.uangku.feature.savings.presentation.component.SavingLogItem
import com.example.uangku.feature.savings.presentation.component.SavingsActionDialog
import com.example.uangku.feature.savings.presentation.component.SavingsFormDialog
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavingsDetailScreen(
    goalId: Long,
    viewModel: SavingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var hasLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(goalId) {
        viewModel.onEvent(SavingsEvent.OnDetailScreenOpen(goalId))
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(SavingsEvent.OnDetailScreenClose)
        }
    }

    LaunchedEffect(state.currentDetailGoal) {
        if (state.currentDetailGoal != null) {
            hasLoaded = true
        } else if (hasLoaded) {
            // Target was deleted, return to previous screen
            onNavigateBack()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(SavingsEvent.ClearError)
        }
    }

    val goal = state.currentDetailGoal

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = "Detail Tabungan",
                onBackClick = onNavigateBack,
                actions = {
                    if (goal != null) {
                        IconButton(onClick = { viewModel.onEvent(SavingsEvent.OnEditGoalClick(goal)) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Ubah Target Tabungan",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(SavingsEvent.OnDeleteGoalClick(goal)) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Target Tabungan",
                                tint = generalRed
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (goal == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kartu Ringkasan Target
                item {
                    DetailGoalCard(goal = goal)
                }

                // Tombol Aksi Nabung & Tarik Dana
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.onEvent(SavingsEvent.OnActionClick(goal, SavingType.DEPOSIT))
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenComponent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Nabung",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.onEvent(SavingsEvent.OnActionClick(goal, SavingType.WITHDRAW))
                            },
                            enabled = goal.currentAmount > 0,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(
                                1.dp,
                                if (goal.currentAmount > 0) generalRed else MaterialTheme.colorScheme.outline
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = generalRed
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Tarik Dana",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Section Judul Riwayat Mutasi
                item {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Riwayat Mutasi",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${state.goalLogs.size} Transaksi",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Daftar Mutasi Log
                if (state.goalLogs.isEmpty()) {
                    item {
                        DetailLogsEmptyState()
                    }
                } else {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                state.goalLogs.forEachIndexed { index, log ->
                                    SavingLogItem(log = log)
                                    if (index < state.goalLogs.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            thickness = 0.6.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
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

    // Dialog Setor & Tarik Dana
    if (state.showActionDialog && state.actionGoal != null) {
        SavingsActionDialog(
            goal = state.actionGoal!!,
            actionType = state.actionType,
            amountInput = state.actionAmountInput,
            descInput = state.actionDescInput,
            onAmountChange = { viewModel.onEvent(SavingsEvent.OnActionAmountChange(it)) },
            onDescChange = { viewModel.onEvent(SavingsEvent.OnActionDescChange(it)) },
            onDismiss = { viewModel.onEvent(SavingsEvent.OnDismissActionDialog) },
            onConfirm = { viewModel.onEvent(SavingsEvent.OnConfirmAction) }
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
fun DetailGoalCard(
    goal: SavingGoal,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")) }
    val progress = goal.progress ?: 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "detail_progress")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Info Baris 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (goal.isTargetReached) greenBg
                                else MaterialTheme.colorScheme.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (goal.isTargetReached) Icons.Default.CheckCircle else Icons.Default.Flag,
                            contentDescription = null,
                            tint = if (goal.isTargetReached) GreenComponent else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column {
                        Text(
                            text = goal.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        goal.targetDate?.let { date ->
                            Spacer(Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Target: ${dateFormat.format(date)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (goal.isTargetReached) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = greenBg,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Tercapai",
                            color = GreenComponent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Baris Terkumpul & Target
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Terkumpul",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = currencyMinimalFormatter(goal.currentAmount),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (goal.isTargetReached) GreenComponent else MaterialTheme.colorScheme.primary
                    )
                }

                goal.targetAmount?.let { target ->
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Target Dana",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = currencyMinimalFormatter(target),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Progress Bar & Remaining (jika ada target)
            goal.targetAmount?.let {
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (goal.isTargetReached) GreenComponent else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}% Tercapai",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    goal.remaining?.let { remaining ->
                        if (remaining > 0) {
                            Text(
                                text = "Kurang ${currencyMinimalFormatter(remaining)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailLogsEmptyState(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Belum Ada Mutasi",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Ketuk tombol 'Nabung' di atas untuk mulai menyisihkan dana ke pos tabungan ini.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
