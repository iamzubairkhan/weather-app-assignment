package com.example.weatherappassignment.view

import androidx.lifecycle.ViewModel
import com.example.weatherappassignment.R
import com.example.weatherappassignment.data.Repository
import com.example.weatherappassignment.data.Result.Success
import com.example.weatherappassignment.data.Result.Error
import com.example.weatherappassignment.utils.ResourceProvider
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
class MainViewModel @Inject constructor(
    private val repository: Repository,
    uiCoroutineContext: CoroutineContext,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val coroutineScope = CoroutineScope(uiCoroutineContext)

    fun refreshData(location: String = "stockholm") {
        coroutineScope.launch {
            when (val result = repository.getCurrentWeather(location = location)) {
                is Success -> {
                    with(result.data) {
                        val currentTemperature = resourceProvider.getString(R.string.current_temperature, temperature)
                        val minTemperature = minTemperature?.let { resourceProvider.getString(R.string.min_temperature, it) }
                        val maxTemperature = maxTemperature?.let { resourceProvider.getString(R.string.max_temperature, it) }
                        _state.update {
                            it.copy(
                                city = cityName,
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
        }
    }

    data class State(
        val city: String? = null,
        val currentCondition: String? = null,
        val currentTemperature: String? = null,
        val minTemperature: String? = null,
        val maxTemperature: String? = null,
        val errorMessage: String? = null
    )

    public override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}