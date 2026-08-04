package com.example.uangku.core.ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(

    title: String
){
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        }
    )
}