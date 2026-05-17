package com.example.weathersnap.data.repository

import com.example.weathersnap.core.common.Result
import com.example.weathersnap.core.util.WeatherCodeMapper
import com.example.weathersnap.data.remote.OpenMeteoForecastApi
import com.example.weathersnap.data.remote.OpenMeteoGeocodingApi
import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val geocodingApi: OpenMeteoGeocodingApi,
    private val forecastApi: OpenMeteoForecastApi
) : WeatherRepository {

    private val suggestionsCache = mutableMapOf<String, List<CitySuggestion>>()

    override suspend fun getCitySuggestions(query: String): Result<List<CitySuggestion>> = withContext(Dispatchers.IO) {
        try {
            suggestionsCache[query]?.let {
                return@withContext Result.Success(it)
            }
            val response = geocodingApi.searchCity(query)
            val suggestions = response.results?.map {
                CitySuggestion(
                    id = it.id,
                    name = it.name,
                    country = it.country,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            } ?: emptyList()
            
            suggestionsCache[query] = suggestions
            Result.Success(suggestions)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun getWeatherForCity(city: CitySuggestion): Result<WeatherSnapshot> = withContext(Dispatchers.IO) {
        try {
            val response = forecastApi.getForecast(city.latitude, city.longitude)
            val current = response.current ?: return@withContext Result.Error("No weather data available")
            
            val snapshot = WeatherSnapshot(
                cityName = city.name,
                country = city.country,
                condition = WeatherCodeMapper.getWeatherCondition(current.weatherCode),
                temperature = current.temperature,
                humidity = current.humidity,
                windSpeed = current.windSpeed,
                pressure = current.pressure
            )
            Result.Success(snapshot)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }
}
