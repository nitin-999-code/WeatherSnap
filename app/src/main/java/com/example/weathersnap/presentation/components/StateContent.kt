package com.example.weathersnap.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StateContent(
    isLoading: Boolean,
    error: String?,
    isEmpty: Boolean,
    emptyMessage: String = "No data available",
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            error != null -> Text(text = error, color = MaterialTheme.colorScheme.error)
            isEmpty -> Text(text = emptyMessage, color = MaterialTheme.colorScheme.onSurfaceVariant)
            else -> content()
        }
    }
}
