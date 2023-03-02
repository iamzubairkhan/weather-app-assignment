package com.example.weatherappassignment.data.remote

import com.google.gson.annotations.SerializedName

data class ApiWeather(
    @SerializedName("main")
    val apiTemperature: ApiTemperature?,
    @SerializedName("weather")
    val apiWeatherConditions: List<ApiWeatherConditions>?
)

data class ApiWeatherConditions(
    @SerializedName("main")
    val condition: String?,
    @SerializedName("icon")
    val icon: String?
)

data class ApiTemperature(
    @SerializedName("temp")
    val currentTemp: Double?,
    @SerializedName("temp_max")
    val maxTemp: Double?,
    @SerializedName("temp_min")
    val minTemp: Double?,
    @SerializedName("humidity")
    val humidity: Int?
)