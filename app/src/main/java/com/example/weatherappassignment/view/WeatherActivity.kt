package com.example.weatherappassignment.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.weatherappassignment.view.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val keyboardController = LocalSoftwareKeyboardController.current
            WeatherAppTheme {
                val state by viewModel.state.collectAsState()
                WeatherScreen(state = state, onSearch = {
                    viewModel.getWeatherData(location = it)
                    keyboardController?.hide()
                })
            }
        }
    }
}