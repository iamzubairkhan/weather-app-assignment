package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.BuildConfig
import com.example.weatherappassignment.data.WeatherDataSource
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.METRIC
import com.example.weatherappassignment.view.WeatherType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class WeatherNetworkDataSourceTest {

    private val mockWeatherApiService = mock<WeatherApiService>()
    private lateinit var weatherDataSource: WeatherDataSource

    @Before
    fun setUp() {
        weatherDataSource = WeatherNetworkDataSource(mockWeatherApiService)
    }

    @Test
    fun `getCurrentWeather returns mapped weather success result when service returns success response`() = runTest {

        // Given
        val expectedCityName = "Stockholm"
        val expectedWeatherCondition = "Clear"
        val apiWeather = ApiWeather(
            apiWeatherConditions = listOf(ApiWeatherConditions(expectedWeatherCondition, icon = "13d")),
            apiTemperature = ApiTemperature(currentTemp = 25.0, minTemp = 20.0, maxTemp = 30.0, humidity = 90)
        )
        val expectedWeather = Weather(
            cityName = expectedCityName,
            condition = expectedWeatherCondition,
            temperature = 25,
            minTemperature = 20,
            maxTemperature = 30,
            humidity = 90,
            weatherType = WeatherType.Snow
        )

        whenever(mockWeatherApiService.getWeatherData(expectedCityName, BuildConfig.API_KEY, METRIC)).thenReturn(apiWeather)

        // When
        val result = weatherDataSource.getCurrentWeather(expectedCityName)

        // Then
        assertThat(result).isEqualTo(Result.success(expectedWeather))
    }

    @Test
    fun `getCurrentWeather throws exception when WeatherData is null`() = runTest {
        // Given
        whenever(mockWeatherApiService.getWeatherData(any(), any(), any())).thenReturn(null)

        // When
        val result = runCatching {
            weatherDataSource.getCurrentWeather("").getOrThrow()
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("WeatherData is null")
    }

    @Test
    fun `getCurrentWeather throws exception when weatherCondition is null`() = runTest {
        // Given
        val apiWeather = ApiWeather(
            apiWeatherConditions = null,
            apiTemperature = ApiTemperature(currentTemp = 0.0, minTemp = 0.0, maxTemp = 0.0, humidity = 90)
        )
        whenever(mockWeatherApiService.getWeatherData(any(), any(), any())).thenReturn(apiWeather)

        // When
        val result = runCatching {
            weatherDataSource.getCurrentWeather("").getOrThrow()
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("weatherCondition is null")
    }

    @Test
    fun `getCurrentWeather throws exception when currentTemp is null`() = runTest {
        // Given
        val apiWeather = ApiWeather(
            apiWeatherConditions = listOf(ApiWeatherConditions("Clouds", "13d")),
            apiTemperature = null
        )
        whenever(mockWeatherApiService.getWeatherData(any(), any(), any())).thenReturn(apiWeather)

        // When
        val result = runCatching {
            weatherDataSource.getCurrentWeather("Stockholm").getOrThrow()
        }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("currentTemp is null")
    }
}