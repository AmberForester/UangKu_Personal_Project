package com.example.uangku.feature.category.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.DialogTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCategoryDialog (
    name: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        DialogTemplate {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Delete Category",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Are you sure to delete \"$name\"?"
                )

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
                        Text("Delete")
                    }

                }
            }
        }
    }
}