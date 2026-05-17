package com.example.weathersnap.presentation.create_report

import com.example.weathersnap.domain.model.WeatherSnapshot

data class CreateReportUiState(
    val weatherSnapshot: WeatherSnapshot? = null,
    val notes: String = "",
    val originalImagePath: String? = null,
    val compressedImagePath: String? = null,
    val originalImageSizeBytes: Long = 0,
    val compressedImageSizeBytes: Long = 0,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)
