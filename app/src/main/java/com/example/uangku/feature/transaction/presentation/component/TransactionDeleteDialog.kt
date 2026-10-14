package com.example.uangku.feature.transaction.presentation.component

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
fun TransactionDeleteDialog(
    title: String,
    message: String,
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
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Batal", color = androidx.compose.ui.graphics.Color(0xFF64748B))
                    }

                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("Hapus", color = com.example.uangku.core.ui.theme.generalRed, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }

                }

            }

        }

    }

}