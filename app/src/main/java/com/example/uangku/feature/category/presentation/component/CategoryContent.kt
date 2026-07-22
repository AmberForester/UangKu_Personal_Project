package com.example.uangku.feature.category.presentation.component

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
            items(state.categories) { category ->
                CategoryItem(
                    category = category,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}