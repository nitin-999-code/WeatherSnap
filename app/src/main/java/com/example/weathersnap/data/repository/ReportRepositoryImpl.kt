package com.example.weathersnap.data.repository

import com.example.weathersnap.data.local.ReportDao
import com.example.weathersnap.data.local.toDomain
import com.example.weathersnap.data.local.toEntity
import com.example.weathersnap.domain.model.WeatherReport
import com.example.weathersnap.domain.repository.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ReportRepositoryImpl(
    private val dao: ReportDao
) : ReportRepository {
    override fun observeReports(): Flow<List<WeatherReport>> {
        return dao.observeReports().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveReport(report: WeatherReport): Long = withContext(Dispatchers.IO) {
        dao.insertReport(report.toEntity())
    }
}
