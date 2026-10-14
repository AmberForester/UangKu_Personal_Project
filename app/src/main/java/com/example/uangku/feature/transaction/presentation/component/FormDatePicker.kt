package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.theme.brandSkyBlue
import com.example.uangku.core.ui.theme.budgetBorderColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    date: Date,
    onDateSelected: (Date) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val formatter = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formatter.format(date),
            onValueChange = {},
            readOnly = true,
            label = { Text("Tanggal Transaksi") },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = brandSkyBlue,
                unfocusedBorderColor = budgetBorderColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = brandSkyBlue,
                unfocusedLabelColor = Color(0xFF64748B)
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = brandSkyBlue
                )
            }
        )

        // Overlay transparan agar seluruh area field bisa diklik
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDialog = true }
        )
    }

    if (showDialog) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.time
        )

        val customDatePickerColors = DatePickerDefaults.colors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF0F172A),
            headlineContentColor = Color(0xFF0F172A),
            weekdayContentColor = Color(0xFF64748B),
            subheadContentColor = Color(0xFF0F172A),
            navigationContentColor = brandSkyBlue,
            yearContentColor = Color(0xFF0F172A),
            currentYearContentColor = brandSkyBlue,
            selectedYearContentColor = Color.White,
            selectedYearContainerColor = brandSkyBlue,
            dayContentColor = Color(0xFF0F172A),
            selectedDayContentColor = Color.White,
            selectedDayContainerColor = brandSkyBlue,
            todayContentColor = brandSkyBlue,
            todayDateBorderColor = brandSkyBlue,
            dividerColor = Color(0xFFF1F5F9)
        )

        DatePickerDialog(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = budgetBorderColor,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = customDatePickerColors,
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        pickerState.selectedDateMillis?.let {
                            onDateSelected(Date(it))
                        }
                        showDialog = false
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = brandSkyBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(end = 12.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Pilih",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "Batal",
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        ) {
            DatePicker(
                state = pickerState,
                showModeToggle = false, // Menyembunyikan tombol pensil agar tampilan kalender tetap bersih
                colors = customDatePickerColors
            )
        }
    }
}
