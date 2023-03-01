package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.WeatherDataSource
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.data.remote.ApiException.ClientException
import com.example.weatherappassignment.data.remote.ApiException.NetworkException
import com.example.weatherappassignment.data.remote.ApiException.ServerException
import com.example.weatherappassignment.data.remote.ApiException.UnknownException
import com.example.weatherappassignment.view.WeatherType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherNetworkDataSource @Inject constructor(private val weatherApiService: WeatherApiService) : WeatherDataSource {
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
        } catch (exception: IOException) {
            Result.failure(NetworkException("Network error \nPlease make sure you are connected to internet", exception))
        } catch (exception: HttpException) {
            Result.failure(
                when (exception.code()) {
                    in 400..499 -> ClientException("Invalid input \nPlease try again", exception)
                    in 500..599 -> ServerException("Something wrong on our end \nPlease try again", exception)
                    else -> UnknownException("Something went wrong \nPlease try again", exception)
                }
            )
        } catch (exception: Exception) {
            Result.failure(UnknownException("Something went wrong \nPlease try again", exception))
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