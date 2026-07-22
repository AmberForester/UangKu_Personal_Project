package com.example.uangku.feature.category.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uangku.feature.category.domain.Category

@Composable
fun CategoryItem(
    category: Category,
    onEditClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    ListItem(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        headlineContent = {
            Text(
                text = category.name,
                color = Color.Black
            )
        },
        trailingContent = {

            Row(){
                IconButton(
                    onClick = {
                        onEditClick(category)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Category"
                    )
                }
                IconButton(
                    onClick = {
                        onDeleteClick(category)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Category"
                    )
                }
            }
        }
    )
}