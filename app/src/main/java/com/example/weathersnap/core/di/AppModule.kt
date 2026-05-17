package com.example.weathersnap.core.di

import com.example.weathersnap.data.local.ReportDao
import com.example.weathersnap.data.remote.OpenMeteoForecastApi
import com.example.weathersnap.data.remote.OpenMeteoGeocodingApi
import com.example.weathersnap.data.repository.ReportRepositoryImpl
import com.example.weathersnap.data.repository.WeatherRepositoryImpl
import com.example.weathersnap.domain.repository.ReportRepository
import com.example.weathersnap.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        geocodingApi: OpenMeteoGeocodingApi,
        forecastApi: OpenMeteoForecastApi
    ): WeatherRepository {
        return WeatherRepositoryImpl(geocodingApi, forecastApi)
    }

    @Provides
    @Singleton
    fun provideReportRepository(dao: ReportDao): ReportRepository {
        return ReportRepositoryImpl(dao)
    }
}
