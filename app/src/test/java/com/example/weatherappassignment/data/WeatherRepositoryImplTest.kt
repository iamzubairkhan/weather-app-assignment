package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather
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
            maxTemperature = 5
        )

        whenever(weatherDataSource.getCurrentWeather(location)).thenReturn(expectedCurrentWeather)

        // When
        val result = weatherRepository.getCurrentWeather(location)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedCurrentWeather)
    }

    @Test
    fun `getCurrentWeather returns Error result when api client throws an exception`() = runTest {

        // Given
        val location = "Stockholm"
        val exception = RuntimeException("Error message")
        whenever(weatherDataSource.getCurrentWeather(location)).thenThrow(exception)

        // When
        val result = weatherRepository.getCurrentWeather(location)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}