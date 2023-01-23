package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.METRIC
import javax.inject.Inject

interface ApiClient {
    suspend fun getCurrentWeather(location: String): Weather
}

class RetrofitApiClient @Inject constructor(private val apiService: ApiService) : ApiClient {
    override suspend fun getCurrentWeather(location: String): Weather {
        return apiService
            .getWeatherData(
                location = location,
                apiKey = BuildConfig.API_KEY,
                temperatureUnit = METRIC
            )
            ?.map(location) ?: throw Exception("WeatherData is null")
    }
}

private fun WeatherData.map(location: String): Weather = Weather(
    cityName = location,
    condition = weatherConditions?.firstOrNull()?.condition ?: throw Exception("weatherCondition is null"),
    temperature = temperature?.currentTemp?.toInt() ?: throw Exception("currentTemp is null"),
    minTemperature = temperature.minTemp?.toInt(),
    maxTemperature = temperature.maxTemp?.toInt()
)