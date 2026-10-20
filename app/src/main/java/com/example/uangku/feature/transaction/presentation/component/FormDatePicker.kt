package com.example.uangku.feature.transaction.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Surface
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
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
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

    val today = remember { LocalDate.now() }
    val yesterday = remember { today.minusDays(1) }

    val currentDate = remember(date) {
        Instant.ofEpochMilli(date.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    val isToday = currentDate == today
    val isYesterday = currentDate == yesterday

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = formatter.format(date),
                onValueChange = {},
                readOnly = true,
                label = { Text("Tanggal Transaksi") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = mainSkyBlue,
                    unfocusedBorderColor = secondaryBgSkyBlue,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = mainSkyBlue,
                    unfocusedLabelColor = Color(0xFF64748B)
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = mainSkyBlue
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

        Spacer(Modifier.height(8.dp))

        // Tombol Tanggal Cepat (Hari Ini & Kemarin)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickDateChip(
                text = "Hari Ini",
                isSelected = isToday,
                onClick = {
                    val dateInstance = Date.from(
                        today.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    )
                    onDateSelected(dateInstance)
                }
            )

            QuickDateChip(
                text = "Kemarin",
                isSelected = isYesterday,
                onClick = {
                    val dateInstance = Date.from(
                        yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    )
                    onDateSelected(dateInstance)
                }
            )
        }
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
            navigationContentColor = mainSkyBlue,
            yearContentColor = Color(0xFF0F172A),
            currentYearContentColor = mainSkyBlue,
            selectedYearContentColor = Color.White,
            selectedYearContainerColor = mainSkyBlue,
            dayContentColor = Color(0xFF0F172A),
            selectedDayContentColor = Color.White,
            selectedDayContainerColor = mainSkyBlue,
            todayContentColor = mainSkyBlue,
            todayDateBorderColor = mainSkyBlue,
            dividerColor = Color(0xFFF1F5F9)
        )

        DatePickerDialog(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = secondaryBgSkyBlue,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = customDatePickerColors,
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        pickerState.selectedDateMillis?.let { utcMillis ->
                            // Konversi UTC millis dari DatePicker ke LocalDate, lalu kembalikan ke Date lokal
                            val localDate = Instant.ofEpochMilli(utcMillis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                            val localDateInstance = Date.from(
                                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                            )
                            onDateSelected(localDateInstance)
                        }
                        showDialog = false
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainSkyBlue,
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

@Composable
private fun QuickDateChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF8FAFC),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) mainSkyBlue else secondaryBgSkyBlue
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) mainSkyBlue else Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}
