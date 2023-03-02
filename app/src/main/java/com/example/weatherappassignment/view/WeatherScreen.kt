package com.example.weatherappassignment.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherappassignment.view.compose.SearchableTextField
import com.example.weatherappassignment.view.compose.WeatherCard
import com.example.weatherappassignment.view.theme.DarkBlue
import com.example.weatherappassignment.view.theme.DeepBlue

@Composable
fun WeatherScreen(
    currentTemperature: String?,
    weatherType: WeatherType?,
    currentCondition: String?,
    minTemperature: String?,
    humidity: String?,
    maxTemperature: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(DarkBlue)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchableTextField(
            searchHint = "Search locations...",
            onSearch = { onSearch(it) }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                ) {
                    WeatherCard(
                        currentTemperature = currentTemperature,
                        weatherType = weatherType,
                        currentCondition = currentCondition,
                        minTemperature = minTemperature,
                        humidity = humidity,
                        maxTemperature = maxTemperature,
                        backgroundColor = DeepBlue
                    )
                }
            }
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewWeatherScreen() {
    WeatherScreen(
        weatherType = WeatherType.ClearSky,
        humidity = "70%",
        currentCondition = "Clear",
        currentTemperature = "5C",
        minTemperature = "-3C",
        maxTemperature = "10C",
        isLoading = false,
        errorMessage = null,
        onSearch = {}
    )
}