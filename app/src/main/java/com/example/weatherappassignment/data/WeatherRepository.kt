package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.Result.Error
import com.example.weatherappassignment.data.Result.Success
import com.example.weatherappassignment.data.model.Weather
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getCurrentWeather(location: String): Result<Weather>
}

class WeatherRepositoryImpl @Inject constructor(private val weatherDataSource: WeatherDataSource) : WeatherRepository {
    override suspend fun getCurrentWeather(location: String): Result<Weather> =
        try {
            val weather = weatherDataSource.getCurrentWeather(location)
            Success(data = weather)
        } catch (exception: Exception) {
            Error(exception)
        }
}