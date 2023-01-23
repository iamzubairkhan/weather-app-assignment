package com.example.weatherappassignment.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherappassignment.R
import com.example.weatherappassignment.data.Repository
import com.example.weatherappassignment.data.Result.Error
import com.example.weatherappassignment.data.Result.Success
import com.example.weatherappassignment.data.model.Weather
import com.example.weatherappassignment.utils.ResourceProvider
import com.example.weatherappassignment.view.MainViewModel.State
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainViewModelTest {

    /*
    * UnconfinedTestDispatcher() generally not recommended to use when you have multiple coroutines in the same function because it does
    * not guarantee ordering. Recommended way is to use StandardTestDispatcher() but due to unknown reasons it was behaving unexpectedly.
    * So, temporarily using UnconfinedTestDispatcher(). It will not harm us as long as we are dealing with single coroutine.
    * TODO("Replace UnconfinedTestDispatcher() when testing a function with multiple coroutines")
    * */
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mock<Repository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            repository = repository,
            uiCoroutineContext = testDispatcher,
            resourceProvider = resourceProvider
        )
    }

    @Test
    fun `refreshData updates state with weather data when API call is successful`() = runTest {
        val location = "stockholm"
        val expectedWeather = Weather(
            cityName = location,
            condition = "Sunny",
            temperature = 25,
            minTemperature = 20,
            maxTemperature = 30
        )

        // Given
        whenever(repository.getCurrentWeather(location)).thenReturn(Success(expectedWeather))
        whenever(resourceProvider.getString(R.string.current_temperature, 25)).thenReturn("25°C")
        whenever(resourceProvider.getString(R.string.min_temperature, 20)).thenReturn("20°C")
        whenever(resourceProvider.getString(R.string.max_temperature, 30)).thenReturn("30°C")

        // When
        viewModel.refreshData(location)

        // Then
        val expectedState = State(
            city = location,
            currentCondition = "Sunny",
            currentTemperature = "25°C",
            minTemperature = "20°C",
            maxTemperature = "30°C",
            errorMessage = null
        )

        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }

    @Test
    fun `refreshData updates state with error message when API call results in error`() = runTest {
        val location = "stockholm"
        val error = Exception("Error message")

        // Given
        whenever(repository.getCurrentWeather(location)).thenReturn(Error(error))

        // When
        viewModel.refreshData(location = location)

        val state = viewModel.state.value

        // Then
        assertThat(state.errorMessage).isEqualTo(error.message)
    }

    @Test
    fun `clear coroutines when view model is cleared`() = runTest {
        // Given
        val job = Job()
        val scope = CoroutineScope(job)
        val viewModel = MainViewModel(repository, scope.coroutineContext, resourceProvider)

        // When
        viewModel.onCleared()

        // Then
        assertThat(job.isCancelled).isTrue()
    }

    @Test
    fun `state shouldShowContentView returns true when errorMessage is null`() {
        // Given
        val state = State()

        // Then
        assertThat(state.shouldShowContentView()).isTrue()
    }

    @Test
    fun `state shouldShowContentView returns false when errorMessage is not null`() {
        // Given
        val state = State(errorMessage = "error")

        // Then
        assertThat(state.shouldShowContentView()).isFalse()
    }

    @Test
    fun `state shouldShowErrorView returns false when errorMessage is null`() {
        // Given
        val state = State()

        // Then
        assertThat(state.shouldShowErrorView()).isFalse()
    }

    @Test
    fun `state shouldShowErrorView returns true when errorMessage is not null`() {
        // Given
        val state = State(errorMessage = "error")

        // Then
        assertThat(state.shouldShowErrorView()).isTrue()
    }
}