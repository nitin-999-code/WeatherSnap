package com.example.weathersnap.domain.repository

import com.example.weathersnap.core.common.Result
import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherSnapshot

interface WeatherRepository {
    suspend fun getCitySuggestions(query: String): Result<List<CitySuggestion>>
    suspend fun getWeatherForCity(city: CitySuggestion): Result<WeatherSnapshot>
}
