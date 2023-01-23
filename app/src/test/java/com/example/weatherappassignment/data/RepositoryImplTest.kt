package com.example.weatherappassignment.data

import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.data.remote.ApiClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private val apiClient = mock<ApiClient>()
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = RepositoryImpl(apiClient)
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

        whenever(apiClient.getCurrentWeather(location)).thenReturn(expectedCurrentWeather)

        // When
        val result = repository.getCurrentWeather(location)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedCurrentWeather)
    }

    @Test
    fun `getCurrentWeather returns Error result when api client throws an exception`() = runTest {

        // Given
        val location = "Stockholm"
        val exception = RuntimeException("Error message")
        whenever(apiClient.getCurrentWeather(location)).thenThrow(exception)

        // When
        val result = repository.getCurrentWeather(location)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}