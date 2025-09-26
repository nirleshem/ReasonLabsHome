package com.example.nir

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.nir.di.AppModule
import com.example.nir.presentation.WeatherScreen

@Composable
fun App() {
    MaterialTheme {
        val viewModel = remember { AppModule.provideWeatherViewModel() }
        WeatherScreen(viewModel = viewModel)
    }
}