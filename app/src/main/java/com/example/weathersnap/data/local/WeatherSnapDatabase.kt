package com.example.weathersnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
abstract class WeatherSnapDatabase : RoomDatabase() {
    abstract val reportDao: ReportDao
    
    companion object {
        const val DATABASE_NAME = "weathersnap_db"
    }
}
