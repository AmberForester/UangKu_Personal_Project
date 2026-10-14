package com.example.uangku.feature.transaction.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.ThousandsSeparatorVisualTransformation
import com.example.uangku.core.ui.component.TopAppBar
import com.example.uangku.core.ui.theme.brandSkyBlue
import com.example.uangku.core.ui.theme.budgetBorderColor
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.transaction.presentation.component.FormCategoryDropdown
import com.example.uangku.feature.transaction.presentation.component.FormDatePicker
import com.example.uangku.feature.transaction.presentation.component.FormTypeSelector
import com.example.uangku.feature.transaction.presentation.component.TransactionDeleteDialog
import com.example.uangku.feature.transaction.presentation.viewModel.TransactionFormViewModel

@RequiresApi(Build.VERSION_CODES.O)
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

    val title = if (id.toInt() == -1) "Tambah Transaksi" else "Edit Transaksi"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = title,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // 1. TIPE TRANSAKSI (PENGELUARAN VS PEMASUKAN)
            FormTypeSelector(
                selectedType = state.type,
                onTypeSelected = {
                    viewModel.onEvent(TransactionFormEvent.OnTypeChange(it))
                }
            )

            Spacer(Modifier.height(16.dp))

            // 2. HERO CARD INPUT NOMINAL
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = budgetBorderColor,
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Nominal Transaksi",
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rp",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                            fontWeight = FontWeight.Bold,
                            color = if (state.type == Type.INCOME) successGreen else generalRed
                        )

                        Spacer(Modifier.width(8.dp))

                        BasicTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.amount,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() }) {
                                    viewModel.onEvent(TransactionFormEvent.OnAmountChange(input))
                                }
                            },
                            singleLine = true,
                            visualTransformation = ThousandsSeparatorVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.type == Type.INCOME) successGreen else generalRed
                            ),
                            decorationBox = { innerTextField ->
                                if (state.amount.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFCBD5E1)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3. DETAIL TRANSAKSI (TANGGAL, KATEGORI, CATATAN)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = budgetBorderColor,
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FormDatePicker(
                        date = state.date,
                        onDateSelected = {
                            viewModel.onEvent(TransactionFormEvent.OnDateChange(it))
                        }
                    )

                    FormCategoryDropdown(
                        categories = state.filteredCategories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = {
                            viewModel.onEvent(TransactionFormEvent.OnCategoryChange(it))
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.description,
                        onValueChange = {
                            viewModel.onEvent(TransactionFormEvent.OnDescriptionChange(it))
                        },
                        label = { Text("Catatan / Keterangan (Opsional)") },
                        placeholder = { Text("Contoh: Makan siang, bensin, bonus") },
                        shape = RoundedCornerShape(14.dp),
                        minLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = brandSkyBlue,
                            unfocusedBorderColor = budgetBorderColor,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = brandSkyBlue,
                            unfocusedLabelColor = Color(0xFF64748B)
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 4. TOMBOL SIMPAN
            val isSaveEnabled = state.amount.isNotBlank() &&
                    state.amount.toDoubleOrNull() != null &&
                    (state.amount.toDoubleOrNull() ?: 0.0) > 0.0 &&
                    state.selectedCategory != null

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    viewModel.onEvent(TransactionFormEvent.OnSave)
                    navController.popBackStack()
                },
                enabled = isSaveEnabled,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brandSkyBlue,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFCBD5E1),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Simpan Transaksi",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.Bold
                )
            }

            // 5. TOMBOL HAPUS (HANYA MUNCUL SAAT EDIT)
            if (state.id != null) {
                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        viewModel.onEvent(TransactionFormEvent.onDeleteClick)
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = generalRed
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Hapus Transaksi",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    ShowDeleteDialog(viewModel, navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDeleteDialog(
    viewModel: TransactionFormViewModel,
    navController: NavController
) {
    val state by viewModel.formState.collectAsState()

    if (state.showDeleteDialog) {
        TransactionDeleteDialog(
            title = "Hapus Transaksi",
            message = "Apakah Anda yakin ingin menghapus transaksi ini?",
            onDismiss = {
                viewModel.onEvent(TransactionFormEvent.OnDeleteDismiss)
            },
            onConfirm = {
                viewModel.onEvent(TransactionFormEvent.OnDeleteConfirm)
                navController.popBackStack()
            }
        )
    }
}
