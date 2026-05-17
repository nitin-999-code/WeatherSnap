package com.example.weathersnap.domain.model

data class WeatherReport(
    val id: Long = 0,
    val cityName: String,
    val country: String?,
    val condition: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Double,
    val notes: String,
    val imagePath: String,
    val originalImageSizeBytes: Long,
    val compressedImageSizeBytes: Long,
    val savedAtMillis: Long
)
