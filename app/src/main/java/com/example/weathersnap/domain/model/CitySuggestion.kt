package com.example.weathersnap.domain.model

data class CitySuggestion(
    val id: Long,
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double
)
