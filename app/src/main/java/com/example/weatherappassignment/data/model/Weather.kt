package com.example.weatherappassignment.data.model

data class Weather(
    val cityName: String,
    val condition: String,
    val temperature: Int,
    val maxTemperature: Int?,
    val minTemperature: Int?
)