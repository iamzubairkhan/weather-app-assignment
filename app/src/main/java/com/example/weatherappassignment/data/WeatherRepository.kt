package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather
import javax.inject.Inject

fun interface WeatherRepository {
    suspend fun getCurrentWeather(location: String): Result<Weather>
}

class WeatherRepositoryImpl @Inject constructor(private val weatherDataSource: WeatherDataSource) : WeatherRepository {
    override suspend fun getCurrentWeather(location: String): Result<Weather> {
        return weatherDataSource.getCurrentWeather(location)
    }
}