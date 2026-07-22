package com.example.uangku.feature.category.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.component.UangKuNavigationBar
import com.example.uangku.feature.category.presentation.component.AddButton
import com.example.uangku.feature.category.presentation.component.CategoryContent
import com.example.uangku.feature.category.presentation.component.CategoryFormDialog
import com.example.uangku.feature.category.presentation.component.DeleteCategoryDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen (
    viewModel: CategoryViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) { viewModel.onEvent(CategoryEvent.onScreenOpen) }
    val state by viewModel.state.collectAsState()

    Log.d("UI", "category = ${state.categories.size}")

    Scaffold (
        topBar = {
            TopAppBar(title = "Category")
        },
        floatingActionButton = {
            AddButton(
                onClick = {
                    viewModel.onEvent(CategoryEvent.onAddClick)
                }
            )
        },
        bottomBar = {
            UangKuNavigationBar(navController = navController)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)){
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
fun ShowFormDialog (viewModel: CategoryViewModel) {
    val state = viewModel.state
    val uiState = state.collectAsState().value
    if(uiState.showFormDialog){
        CategoryFormDialog(
            title = (if (uiState.selectedCategory == null) "Tambah Kategori" else "Edit Kategori" ).toString(),
            confirmButtonText = "Save",
            name = uiState.name,
            type = uiState.type,
            onNameChange = {
                viewModel.onEvent(CategoryEvent.onNameChange(it))
            },
            onTypeChange = {
                viewModel.onEvent(CategoryEvent.onTypeChange(it))
            },
            onDismiss = { viewModel.onEvent(CategoryEvent.onDismissFormDialog) },
            onConfirm = { viewModel.onEvent(CategoryEvent.onSave)}
        )
    }
}

@Composable
fun ShowDeleteDialog (viewModel: CategoryViewModel) {
    val state = viewModel.state
    val uiState = state.collectAsState().value
    if(uiState.showDeleteDialog){
        DeleteCategoryDialog(
            name = uiState.name,
            onDismiss = { viewModel.onEvent(CategoryEvent.onDismissDeleteDialog) },
            onConfirm = { viewModel.onEvent(CategoryEvent.onDeleteConfirm) }
        )
    }
}



