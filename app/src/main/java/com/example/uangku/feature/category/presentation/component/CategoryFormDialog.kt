package com.example.uangku.feature.category.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.DialogTemplate
import com.example.uangku.core.domain.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFormDialog (
    title: String,
    confirmButtonText: String,
    name: String,
    type: Type,
    onNameChange: (String) -> Unit,
    onTypeChange: (Type) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        DialogTemplate {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = onNameChange,
                    label = {
                        Text("Category Name")
                    },
                    singleLine = true
                )

                if(title.equals("tambah kategori", ignoreCase = true)){
                    Text(
                        text = "Category Type",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == Type.EXPENSE,
                            onClick = {
                                onTypeChange(Type.EXPENSE)
                            }
                        )
                        Text("Expense")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == Type.INCOME,
                            onClick = {
                                onTypeChange(Type.INCOME)
                            }
                        )
                        Text("Income")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }

                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text(confirmButtonText)
                    }
                }
            }
        }
    }
}