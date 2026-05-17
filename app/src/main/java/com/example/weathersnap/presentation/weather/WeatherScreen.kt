package com.example.weathersnap.presentation.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.presentation.components.AppHeader
import com.example.weathersnap.presentation.components.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onNavigateToReports: () -> Unit,
    onNavigateToCreateReport: (WeatherSnapshot) -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppHeader(
            title = "WeatherSnap",
            subtitle = "Live weather reporting with camera evidence",
            actionText = "Reports",
            onActionClick = onNavigateToReports
        )

        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city...") },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Enter more than 2 letters to start city suggestions.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = state.suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(state.suggestions) { city ->
                        Text(
                            text = "${city.name}${if (city.country != null) ", ${city.country}" else ""}",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onCitySelected(city) }
                                .padding(16.dp)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.surface)
                    }
                }
            }
        }

        AnimatedContent(targetState = state, label = "weather_state") { targetState ->
            when {
                targetState.isLoadingWeather -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                targetState.weatherError != null -> {
                    Text(
                        text = targetState.weatherError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                targetState.weatherSnapshot != null -> {
                    Column {
                        WeatherCard(snapshot = targetState.weatherSnapshot)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onNavigateToCreateReport(targetState.weatherSnapshot) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                "Create Report", 
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
