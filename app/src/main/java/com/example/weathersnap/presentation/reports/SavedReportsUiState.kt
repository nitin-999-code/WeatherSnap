package com.example.weathersnap.presentation.reports

import com.example.weathersnap.domain.model.WeatherReport

data class SavedReportsUiState(
    val reports: List<WeatherReport> = emptyList(),
    val isLoading: Boolean = true
)
