package com.example.weathersnap.presentation.weather

import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherSnapshot

data class WeatherUiState(
    val query: String = "",
    val suggestions: List<CitySuggestion> = emptyList(),
    val isSearchingSuggestions: Boolean = false,
    val selectedCity: CitySuggestion? = null,
    val weatherSnapshot: WeatherSnapshot? = null,
    val isLoadingWeather: Boolean = false,
    val weatherError: String? = null
)
