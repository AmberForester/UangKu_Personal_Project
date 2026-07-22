package com.example.uangku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.uangku.core.dependencyInjection.AppContainer
import com.example.uangku.core.navigation.UangKuNavGraph
import com.example.uangku.feature.category.presentation.CategoryViewModel
import com.example.uangku.feature.category.presentation.CategoryViewModelFactory
import com.example.uangku.core.ui.theme.UangKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer =(application as App).appContainer

        setContent {
            UangKuTheme {
                val categoryViewModel: CategoryViewModel = viewModel(
                    factory = CategoryViewModelFactory(
                        appContainer.categoryUseCase
                    )
                )

                val navController = rememberNavController()

                UangKuNavGraph(
                    navController = navController,
                    categoryViewModel = categoryViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UangKuTheme {
        Greeting("Android")
    }
}