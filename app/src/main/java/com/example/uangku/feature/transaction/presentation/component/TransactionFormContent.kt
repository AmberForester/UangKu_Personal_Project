package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TransactionFormContent(
    onAmountChange:(String) -> Unit,
    amount: String
) {
    Box {
        Text(
            text = "Jumlah",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount,
            onValueChange = onAmountChange,
            label = {

            },
            singleLine = true
        )
    }
}