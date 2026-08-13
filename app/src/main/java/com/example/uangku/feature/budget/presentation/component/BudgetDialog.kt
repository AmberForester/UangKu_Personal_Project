package com.example.uangku.feature.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.DialogTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDialog(

    categoryName: String,

    amount: String,

    errorMessage: String?,

    onAmountChange: (String) -> Unit,

    onDismiss: () -> Unit,

    onSave: () -> Unit

) {

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {

        DialogTemplate {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (amount == "0") "" else amount,
                    onValueChange = onAmountChange,
                    singleLine = true,
                    label = {

                        Text("Monthly Budget")

                    },

                    keyboardOptions = KeyboardOptions(

                        keyboardType = KeyboardType.Number
                    ),
                    supportingText = {
                        if(errorMessage != null) {
                            Text(errorMessage)
                        }
                    }
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

                    Button(
                        onClick = onSave
                    ) {
                        Text("Save")
                    }

                }
            }
        }
    }
}