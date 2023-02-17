package com.example.weatherappassignment.data.model

import com.example.weatherappassignment.view.WeatherType

data class Weather(
    val cityName: String,
    val condition: String,
    val temperature: Int,
    val maxTemperature: Int?,
    val minTemperature: Int?,
    val humidity: Int?,
    val weatherType: WeatherType?
)