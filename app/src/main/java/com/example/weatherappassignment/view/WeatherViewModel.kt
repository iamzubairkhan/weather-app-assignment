package com.example.weatherappassignment.view

import androidx.lifecycle.ViewModel
import com.example.weatherappassignment.R
import com.example.weatherappassignment.data.Result.Error
import com.example.weatherappassignment.data.Result.Success
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

    fun refreshState() = _state.update {
        it.copy(
            city = null,
            currentTemperature = null,
            currentCondition = null,
            minTemperature = null,
            maxTemperature = null,
            isLoading = false,
            errorMessage = null
        )
    }

    fun getWeatherData(location: String) {
        if (location.isBlank()) {
            _state.update { it.copy(errorMessage = "Error: Location cannot be empty") }
            return
        }
        coroutineScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = weatherRepository.getCurrentWeather(location = location)) {
                is Success -> {
                    with(result.data) {
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
                                errorMessage = null
                            )
                        }
                    }
                }
                is Error -> _state.update { it.copy(errorMessage = result.exception.localizedMessage) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    data class State(
        val city: String? = null,
        val currentCondition: String? = null,
        val currentTemperature: String? = null,
        val minTemperature: String? = null,
        val maxTemperature: String? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) {
        fun shouldShowContentView(): Boolean = errorMessage == null
        fun shouldShowErrorView(): Boolean = errorMessage != null
    }

    public override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}