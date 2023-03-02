package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.METRIC

interface WeatherDataSource {
    suspend fun getCurrentWeather(location: String, temperatureUnit: String = METRIC): Result<Weather>
}
