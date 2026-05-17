package com.example.weathersnap.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.core.common.Result
import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query, suggestions = emptyList(), weatherSnapshot = null, weatherError = null, selectedCity = null) }
        searchJob?.cancel()
        if (query.length > 2) {
            searchJob = viewModelScope.launch {
                delay(500)
                _uiState.update { it.copy(isSearchingSuggestions = true) }
                when (val result = repository.getCitySuggestions(query)) {
                    is Result.Success -> {
                        _uiState.update { it.copy(suggestions = result.data, isSearchingSuggestions = false) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isSearchingSuggestions = false) }
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    fun onCitySelected(city: CitySuggestion) {
        _uiState.update { 
            it.copy(
                selectedCity = city, 
                suggestions = emptyList(),
                query = "${city.name}${if (city.country != null) ", ${city.country}" else ""}",
                isLoadingWeather = true,
                weatherError = null,
                weatherSnapshot = null
            ) 
        }
        viewModelScope.launch {
            when (val result = repository.getWeatherForCity(city)) {
                is Result.Success -> {
                    _uiState.update { it.copy(weatherSnapshot = result.data, isLoadingWeather = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(weatherError = result.message, isLoadingWeather = false) }
                }
                is Result.Loading -> {}
            }
        }
    }
}
