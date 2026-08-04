package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.uangku.feature.category.domain.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selectedCategory?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Category")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(category.name)
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}