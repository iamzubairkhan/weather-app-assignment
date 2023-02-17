package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.WeatherDataSource
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.METRIC
import com.example.weatherappassignment.view.WeatherType
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherNetworkDataSource @Inject constructor(private val weatherApiService: WeatherApiService) : WeatherDataSource {
    override suspend fun getCurrentWeather(location: String): Weather {
        return weatherApiService
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
    temperature = temperature?.currentTemp?.roundToInt() ?: throw Exception("currentTemp is null"),
    minTemperature = temperature.minTemp?.roundToInt(),
    maxTemperature = temperature.maxTemp?.roundToInt(),
    humidity = temperature.humidity,
    weatherType = WeatherType.fromCode(weatherConditions.firstOrNull()?.icon)
)