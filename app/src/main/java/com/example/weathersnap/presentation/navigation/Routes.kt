package com.example.weathersnap.presentation.navigation

sealed class Routes(val route: String) {
    object Weather : Routes("weather")
    object CreateReport : Routes("create_report/{snapshotJson}") {
        fun createRoute(snapshotJson: String) = "create_report/$snapshotJson"
    }
    object Camera : Routes("camera")
    object SavedReports : Routes("reports")
}
