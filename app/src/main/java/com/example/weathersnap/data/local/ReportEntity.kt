package com.example.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathersnap.domain.model.WeatherReport

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
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

fun ReportEntity.toDomain(): WeatherReport {
    return WeatherReport(
        id = id,
        cityName = cityName,
        country = country,
        condition = condition,
        temperature = temperature,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        notes = notes,
        imagePath = imagePath,
        originalImageSizeBytes = originalImageSizeBytes,
        compressedImageSizeBytes = compressedImageSizeBytes,
        savedAtMillis = savedAtMillis
    )
}

fun WeatherReport.toEntity(): ReportEntity {
    return ReportEntity(
        id = id,
        cityName = cityName,
        country = country,
        condition = condition,
        temperature = temperature,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        notes = notes,
        imagePath = imagePath,
        originalImageSizeBytes = originalImageSizeBytes,
        compressedImageSizeBytes = compressedImageSizeBytes,
        savedAtMillis = savedAtMillis
    )
}
