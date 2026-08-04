package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uangku.core.domain.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTypeSelector(
    selectedType: Type,
    onTypeSelected: (Type) -> Unit
) {

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {

        SegmentedButton(
            selected = selectedType == Type.EXPENSE,
            onClick = {
                onTypeSelected(Type.EXPENSE)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 2
            )
        ) {
            Text("Expense")
        }

        SegmentedButton(
            selected = selectedType == Type.INCOME,
            onClick = {
                onTypeSelected(Type.INCOME)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 2
            )
        ) {
            Text("Income")
        }

    }

}