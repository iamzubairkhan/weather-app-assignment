package com.example.weatherappassignment.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
        @Query("units") temperatureUnit: String
    ): WeatherData?
}