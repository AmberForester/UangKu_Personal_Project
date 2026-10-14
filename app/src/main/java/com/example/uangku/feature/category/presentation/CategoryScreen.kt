package com.example.uangku.feature.category.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.feature.category.presentation.component.AddButton
import com.example.uangku.feature.category.presentation.component.CategoryContent
import com.example.uangku.feature.category.presentation.component.CategoryFormDialog
import com.example.uangku.feature.category.presentation.component.DeleteCategoryDialog
import com.example.uangku.feature.transaction.presentation.component.FormTypeSelector

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) { viewModel.onEvent(CategoryEvent.onScreenOpen) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = "Kategori Transaksi",
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            AddButton(
                onClick = {
                    viewModel.onEvent(CategoryEvent.onAddClick)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(Modifier.height(8.dp))

            FormTypeSelector(
                onTypeSelected = {
                    viewModel.onEvent(CategoryEvent.onSelectedTypeChange(it))
                },
                selectedType = state.selectedType,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(14.dp))

            // Subtitle info total kategori
            val typeLabel = if (state.selectedType == Type.EXPENSE) "Pengeluaran" else "Pemasukan"
            Text(
                text = "${state.filteredCategory.size} Kategori $typeLabel",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.5.sp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )

            CategoryContent(
                state = state,
                onEditClick = {
                    viewModel.onEvent(CategoryEvent.onEditClick(it))
                },
                onDeleteClick = {
                    viewModel.onEvent(CategoryEvent.onDeleteClick(it))
                }
            )
        }
    }

    ShowFormDialog(viewModel)
    ShowDeleteDialog(viewModel)
}

@Composable
fun ShowFormDialog(viewModel: CategoryViewModel) {
    val state = viewModel.state
    val uiState = state.collectAsState().value
    if (uiState.showFormDialog) {
        CategoryFormDialog(
            title = if (uiState.selectedCategory == null) "Tambah Kategori" else "Edit Kategori",
            confirmButtonText = "Simpan",
            name = uiState.name,
            type = uiState.type,
            onNameChange = {
                viewModel.onEvent(CategoryEvent.onNameChange(it))
            },
            onTypeChange = {
                viewModel.onEvent(CategoryEvent.onTypeChange(it))
            },
            onDismiss = { viewModel.onEvent(CategoryEvent.onDismissFormDialog) },
            onConfirm = { viewModel.onEvent(CategoryEvent.onSave) }
        )
    }
}

@Composable
fun ShowDeleteDialog(viewModel: CategoryViewModel) {
    val state = viewModel.state
    val uiState = state.collectAsState().value
    if (uiState.showDeleteDialog) {
        DeleteCategoryDialog(
            name = uiState.name,
            onDismiss = { viewModel.onEvent(CategoryEvent.onDismissDeleteDialog) },
            onConfirm = { viewModel.onEvent(CategoryEvent.onDeleteConfirm) }
        )
    }
}
