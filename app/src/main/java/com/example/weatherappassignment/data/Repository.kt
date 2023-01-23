package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.Result.Error
import com.example.weatherappassignment.data.Result.Success
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.data.remote.ApiClient
import javax.inject.Inject

interface Repository {
    suspend fun getCurrentWeather(location: String): Result<Weather>
}

class RepositoryImpl @Inject constructor(private val apiClient: ApiClient) : Repository {
    override suspend fun getCurrentWeather(location: String): Result<Weather> =
        try {
            val weather = apiClient.getCurrentWeather(location)
            Success(data = weather)
        } catch (exception: Exception) {
            Error(exception)
        }
}