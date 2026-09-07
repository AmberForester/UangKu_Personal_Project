package com.example.uangku.feature.period.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodDialog(

    selectedDate: Int,
    onDismissDialog: () -> Unit,
    onConfirmDialog: (Int) -> Unit

) {
    var selected by remember { mutableIntStateOf(selectedDate) }

     BasicAlertDialog(
         modifier = Modifier
             .fillMaxWidth(),
         onDismissRequest = onDismissDialog,
     ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Atur tanggal mulai periode",
                style = MaterialTheme.typography.titleMedium
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),

                content = {
                    items(31) { index ->
                        val date = index + 1
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color =
                                    if (date == selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable {
                                    selected = date
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.toString(),
                                color =
                                if (date == selectedDate) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            )

            Text(
                text = "Periode dimulai setiap tanggal $selectedDate.",
                modifier = Modifier
                    .padding(
                        top = 16.dp
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = onDismissDialog
                    ) {
                        Text("Batal")
                    }

                    TextButton(
                        onClick = { onConfirmDialog(selected) }
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
     }
}