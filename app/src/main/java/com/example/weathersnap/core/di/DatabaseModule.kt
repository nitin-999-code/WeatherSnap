package com.example.weathersnap.core.di

import android.app.Application
import androidx.room.Room
import com.example.weathersnap.data.local.WeatherSnapDatabase
import com.example.weathersnap.data.local.ReportDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): WeatherSnapDatabase {
        return Room.databaseBuilder(
            app,
            WeatherSnapDatabase::class.java,
            WeatherSnapDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDao(db: WeatherSnapDatabase): ReportDao {
        return db.reportDao
    }
}
