package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    date: Date,
    onDateSelected: (Date) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    val formatter = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            },
        value = formatter.format(date),
        onValueChange = {},
        readOnly = true,
        label = {
            Text("Date")
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null
                )
            }
        }
    )

    if (showDialog) {

        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.time
        )

        DatePickerDialog(

            onDismissRequest = {
                showDialog = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        pickerState.selectedDateMillis?.let {

                            onDateSelected(
                                Date(it)
                            )

                        }

                        showDialog = false

                    }

                ) {

                    Text("OK")

                }

            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {

                    Text("Cancel")

                }

            }

        ) {

            DatePicker(
                state = pickerState
            )

        }

    }

}