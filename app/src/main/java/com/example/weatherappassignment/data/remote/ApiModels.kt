package com.example.weatherappassignment.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("main")
    val temperature: Temperature?,
    @SerializedName("weather")
    val weatherConditions: List<WeatherConditions>?
)

data class WeatherConditions(
    @SerializedName("main")
    val condition: String?,
    @SerializedName("icon")
    val icon: String?
)

data class Temperature(
    @SerializedName("temp")
    val currentTemp: Double?,
    @SerializedName("temp_max")
    val maxTemp: Double?,
    @SerializedName("temp_min")
    val minTemp: Double?,
    @SerializedName("humidity")
    val humidity: Int?
)