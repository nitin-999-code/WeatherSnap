package com.example.weathersnap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherSnapshot(
    val cityName: String,
    val country: String?,
    val condition: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Double
) : Parcelable
