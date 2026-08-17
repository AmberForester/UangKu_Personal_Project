package com.example.uangku.feature.category.presentation.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uangku.feature.category.domain.Category
import com.example.uangku.feature.category.presentation.CategoryState

@Composable
fun CategoryContent(
    state: CategoryState,
    onEditClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    if( state.categories.isEmpty()){
        CategoryEmptyState()
    } else {
        Log.d("UI", "Show list")

        LazyColumn {
            items(state.filteredCategory) { category ->
                CategoryItem(
                    category = category,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
fun CategoryEmptyState() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.FolderOpen,
            contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "No categories yet",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Tap the + button to create your first category.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}