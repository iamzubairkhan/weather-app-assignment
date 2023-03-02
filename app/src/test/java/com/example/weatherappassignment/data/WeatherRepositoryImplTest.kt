package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.view.WeatherType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class WeatherRepositoryImplTest {

    private val weatherDataSource = mock<WeatherDataSource>()
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        weatherRepository = WeatherRepositoryImpl(weatherDataSource)
    }

    @Test
    fun `getCurrentWeather returns Success result when api client returns successful response`() = runTest {

        // Given
        val location = "Stockholm"
        val expectedCurrentWeather = Weather(
            cityName = location,
            condition = "Clouds",
            temperature = 0,
            minTemperature = -5,
            maxTemperature = 5,
            humidity = 90,
            weatherType = WeatherType.Cloudy
        )

        whenever(weatherDataSource.getCurrentWeather(location)).thenReturn(Result.success(expectedCurrentWeather))

        // When
        val result = weatherRepository.getCurrentWeather(location).getOrNull()

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(expectedCurrentWeather)
    }

    @Test
    fun `getCurrentWeather returns Error result when api client throws an exception`() = runTest {

        // Given
        val location = "Stockholm"
        val exception = RuntimeException("Error message")
        whenever(weatherDataSource.getCurrentWeather(location)).thenReturn(Result.failure(exception))

        // When
        val result = runCatching { weatherRepository.getCurrentWeather(location).getOrThrow() }.exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
        assertThat(result).hasMessageThat().isEqualTo("Error message")
    }
}