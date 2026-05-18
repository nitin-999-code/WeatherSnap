package com.example.weathersnap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathersnap.presentation.weather.WeatherScreen
import com.example.weathersnap.presentation.create_report.CreateReportScreen
import com.example.weathersnap.presentation.camera.CameraScreen
import com.example.weathersnap.presentation.reports.SavedReportsScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Weather.route) {
        composable(Routes.Weather.route) {
            WeatherScreen(
                onNavigateToReports = {
                    navController.navigate(Routes.SavedReports.route)
                },
                onNavigateToCreateReport = { snapshot ->
                    val json = Gson().toJson(snapshot)
                    val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.CreateReport.createRoute(encoded))
                }
            )
        }
        composable(Routes.CreateReport.route) { backStackEntry ->
            CreateReportScreen(
                navBackStackEntry = backStackEntry,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(Routes.Camera.route) },
                onNavigateToReports = {
                    navController.popBackStack(Routes.Weather.route, false)
                    navController.navigate(Routes.SavedReports.route)
                }
            )
        }
        composable(Routes.Camera.route) {
            CameraScreen(
                onClose = { navController.popBackStack() },
                onImageCaptured = { path ->
                    // Return result to previous screen
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("captured_image_path", path)
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SavedReports.route) {
            SavedReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
