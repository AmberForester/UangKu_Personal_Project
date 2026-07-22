package com.example.uangku.feature.category.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddButton(
    onClick: () -> Unit
){
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .size(60.dp)
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add category",
        )
    }
}
