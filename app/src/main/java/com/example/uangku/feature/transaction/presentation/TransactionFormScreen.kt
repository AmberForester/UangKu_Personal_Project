package com.example.uangku.feature.transaction.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.feature.transaction.presentation.component.FormCategoryDropdown
import com.example.uangku.feature.transaction.presentation.component.FormDatePicker
import com.example.uangku.feature.transaction.presentation.component.FormTypeSelector
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel

@Composable
fun TransactionFormScreen(
    id: Long,
    viewModel: TransactionFormViewModel,
    navController: NavController
) {

    val state by viewModel.formState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            TransactionFormEvent.OnScreenOpen(id = id)
        )
    }

    val title = if(id.toInt() == -1) "Add Transaction" else "Edit Transaction"

    Scaffold(

        topBar = {
            TopAppBar(title = title)
        },

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            FormTypeSelector(
                selectedType = state.type,
                onTypeSelected = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnTypeChange(it)
                    )
                }
            )
            
            FormDatePicker(
                date = state.date,
                onDateSelected = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnDateChange(it)
                    )
                }
            )

            FormCategoryDropdown(
                categories = state.filteredCategories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnCategoryChange(it)
                    )
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.amount.toString(),
                onValueChange = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnAmountChange(it)
                    )
                },
                label = {
                    Text("Amount")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnDescriptionChange(it)
                    )
                },
                label = {
                    Text("Description")
                },
                minLines = 3
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(
                        TransactionFormEvent.OnSave
                    )
                    navController.popBackStack()
                },
            ) {
                Text("Save")
            }
        }
    }
}