package com.example.weathersnap.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("current") val current: CurrentWeatherDto?
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("weather_code") val weatherCode: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("pressure_msl") val pressure: Double
)
