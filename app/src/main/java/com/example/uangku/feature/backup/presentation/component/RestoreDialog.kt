package com.example.uangku.feature.backup.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoreDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    if (!showDialog) return

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Restore Data?"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Data saat ini akan hilang dan digantikan dengan data dari file"
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Batal")
                    }

                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("Lanjutkan")
                    }
                }
            }
        }
    }
}