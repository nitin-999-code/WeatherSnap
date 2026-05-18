package com.example.weathersnap.presentation.create_report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberAsyncImagePainter
import com.example.weathersnap.presentation.components.AppHeader
import com.example.weathersnap.presentation.components.WeatherCard
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    navBackStackEntry: NavBackStackEntry,
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToReports: () -> Unit,
    viewModel: CreateReportViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val capturedImagePath by navBackStackEntry.savedStateHandle.getStateFlow<String?>("captured_image_path", null).collectAsState()

    LaunchedEffect(capturedImagePath) {
        capturedImagePath?.let { path ->
            viewModel.onImageCaptured(path)
            navBackStackEntry.savedStateHandle.remove<String>("captured_image_path")
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            onNavigateToReports()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(
            title = "Create Report",
            subtitle = "Capture, compress, annotate",
            actionText = "Back",
            onActionClick = onNavigateBack
        )

        state.weatherSnapshot?.let {
            WeatherCard(snapshot = it)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (state.compressedImagePath != null) {
                Image(
                    painter = rememberAsyncImagePainter(File(state.compressedImagePath!!)),
                    contentDescription = "Captured image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Photo preview", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToCamera,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (state.compressedImagePath == null) "Capture Photo" else "Retake Photo")
        }

        if (state.compressedImagePath != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Original: ${state.originalImageSizeBytes / 1024} KB",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Compressed: ${state.compressedImageSizeBytes / 1024} KB",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.notes,
            onValueChange = viewModel::onNotesChange,
            modifier = Modifier.fillMaxWidth().height(120.dp),
            placeholder = { Text("Add notes here...") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::saveReport,
            enabled = !state.isSaving && state.compressedImagePath != null,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Save Report", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
