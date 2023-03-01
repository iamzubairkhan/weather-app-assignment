package com.example.weatherappassignment.view

import androidx.lifecycle.ViewModel
import com.example.weatherappassignment.R
import com.example.weatherappassignment.data.WeatherRepository
import com.example.weatherappassignment.utils.ResourceProvider
import com.example.weatherappassignment.utils.capitalized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    uiCoroutineContext: CoroutineContext,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val coroutineScope = CoroutineScope(uiCoroutineContext)

    fun getWeatherData(location: String) {
        _state.update { State() }
        if (location.isBlank()) {
            _state.update { it.copy(errorMessage = "No location entered \nPlease enter valid location") }
            return
        }
        coroutineScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val result = weatherRepository.getCurrentWeather(location = location).getOrThrow()
                with(result) {
                    val currentTemperature = resourceProvider.getString(R.string.current_temperature, temperature)
                    val minTemperature = minTemperature?.let { resourceProvider.getString(R.string.min_temperature, it) }
                    val maxTemperature = maxTemperature?.let { resourceProvider.getString(R.string.max_temperature, it) }
                    _state.update {
                        it.copy(
                            city = cityName.capitalized(),
                            currentCondition = condition,
                            currentTemperature = currentTemperature,
                            minTemperature = minTemperature,
                            maxTemperature = maxTemperature,
                            humidity = "$humidity%",
                            weatherType = weatherType
                        )
                    }
                }
            } catch (exception: Exception) {
                _state.update { it.copy(errorMessage = exception.message) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    data class State(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val city: String? = null,
        val currentCondition: String? = null,
        val currentTemperature: String? = null,
        val minTemperature: String? = null,
        val maxTemperature: String? = null,
        val humidity: String? = null,
        val weatherType: WeatherType? = null
    ) {
        fun shouldShowContentView(): Boolean = errorMessage == null
        fun shouldShowErrorView(): Boolean = errorMessage != null
    }

    public override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}