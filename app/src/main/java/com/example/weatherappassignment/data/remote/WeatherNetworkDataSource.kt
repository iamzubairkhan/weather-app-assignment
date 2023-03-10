package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.WeatherDataSource
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.view.WeatherType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherNetworkDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val errorMapper: ErrorMapper
) : WeatherDataSource {
    override suspend fun getCurrentWeather(location: String, temperatureUnit: String): Result<Weather> = withContext(Dispatchers.IO) {
        try {
            val weather = weatherApiService
                .getWeatherData(
                    location = location,
                    temperatureUnit = temperatureUnit,
                    apiKey = BuildConfig.API_KEY
                )
                .map(location)
            Result.success(value = weather)
        } catch (exception: Exception) {
            Result.failure(exception = errorMapper.map(exception))
        }
    }
}

private fun ApiWeather.map(location: String): Weather = Weather(
    cityName = location,
    condition = apiWeatherConditions?.firstOrNull()?.condition ?: throw Exception("weatherCondition is null"),
    temperature = apiTemperature?.currentTemp?.roundToInt() ?: throw Exception("currentTemp is null"),
    minTemperature = apiTemperature.minTemp?.roundToInt(),
    maxTemperature = apiTemperature.maxTemp?.roundToInt(),
    humidity = apiTemperature.humidity,
    weatherType = WeatherType.fromCode(apiWeatherConditions.firstOrNull()?.icon)
)