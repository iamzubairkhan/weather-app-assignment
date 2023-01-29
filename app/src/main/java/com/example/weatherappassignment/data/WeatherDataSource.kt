package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather

interface WeatherDataSource {
    suspend fun getCurrentWeather(location: String): Weather
}
