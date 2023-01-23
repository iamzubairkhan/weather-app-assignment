package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.METRIC
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RetrofitApiClientTest {

    private val mockApiService = mock<ApiService>()
    private lateinit var apiClient: ApiClient

    @Before
    fun setUp() {
        apiClient = RetrofitApiClient(mockApiService)
    }

    @Test
    fun `getCurrentWeather returns mapped Weather object when service returns success response`() = runTest {

        // Given
        val expectedCityName = "Stockholm"
        val expectedWeatherCondition = "Clear"
        val weatherData = WeatherData(
            weatherConditions = listOf(WeatherConditions(expectedWeatherCondition)),
            temperature = Temperature(currentTemp = 25.0, minTemp = 20.0, maxTemp = 30.0)
        )
        val expectedWeather = Weather(
            cityName = expectedCityName,
            condition = expectedWeatherCondition,
            temperature = 25,
            minTemperature = 20,
            maxTemperature = 30
        )

        whenever(mockApiService.getWeatherData(expectedCityName, BuildConfig.API_KEY, METRIC)).thenReturn(weatherData)

        // When
        val result = apiClient.getCurrentWeather(expectedCityName)

        // Then
        assertThat(result).isEqualTo(expectedWeather)
    }

    @Test
    fun `getCurrentWeather throws exception when WeatherData is null`() = runTest {
        // Given
        whenever(mockApiService.getWeatherData(any(), any(), any())).thenReturn(null)

        // When
        val result = runCatching {
            apiClient.getCurrentWeather("")
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("WeatherData is null")
    }

    @Test
    fun `getCurrentWeather throws exception when weatherCondition is null`() = runTest {
        // Given
        val weatherData = WeatherData(
            weatherConditions = null,
            temperature = Temperature(currentTemp = 0.0, minTemp = 0.0, maxTemp = 0.0)
        )
        whenever(mockApiService.getWeatherData(any(), any(), any())).thenReturn(weatherData)

        // When
        val result = runCatching {
            apiClient.getCurrentWeather("")
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("weatherCondition is null")
    }

    @Test
    fun `getCurrentWeather throws exception when currentTemp is null`() = runTest {
        // Given
        val weatherData = WeatherData(
            weatherConditions = listOf(WeatherConditions("Clouds")),
            temperature = null
        )
        whenever(mockApiService.getWeatherData(any(), any(), any())).thenReturn(weatherData)

        // When
        val result = runCatching {
            apiClient.getCurrentWeather("Stockholm")
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("currentTemp is null")
    }
}