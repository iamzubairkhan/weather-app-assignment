package com.example.weatherappassignment.data

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class ApiServiceTest {

    private lateinit var service: ApiService
    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `getWeatherData returns valid WeatherData when API returns valid response`() = runTest {

        // Given
        val location = "stockholm"
        val expectedTemperature = Temperature(currentTemp = 0.0, minTemp = -5.0, maxTemp = 5.0)
        val expectedWeatherConditions = listOf(WeatherConditions(condition = "Clouds"))
        val expectedWeatherData = WeatherData(temperature = expectedTemperature, weatherConditions = expectedWeatherConditions)

        mockServer.enqueue(MockResponse().setBody(Gson().toJson(expectedWeatherData)))

        // When
        val result = service.getWeatherData(location = location, "API_KEY", "METRIC")

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.weatherConditions?.firstOrNull()?.condition).isEqualTo("Clouds")
        assertThat(result?.temperature?.currentTemp).isEqualTo(0.0)
        assertThat(result?.temperature?.minTemp).isEqualTo(-5.0)
        assertThat(result?.temperature?.maxTemp).isEqualTo(5.0)
    }

    @Test
    fun `getWeatherData returns null when API returns invalid response`() = runTest {
        // Given
        mockServer.enqueue(MockResponse().setResponseCode(404))

        // When
        val result = runCatching { service.getWeatherData("Stockholm", "API_KEY", "METRIC") }
            .exceptionOrNull()

        // Then
        assertThat(result).isInstanceOf(Exception::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}