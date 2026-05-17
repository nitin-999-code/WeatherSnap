package com.example.weathersnap.domain.repository

import com.example.weathersnap.domain.model.WeatherReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun observeReports(): Flow<List<WeatherReport>>
    suspend fun saveReport(report: WeatherReport): Long
}
