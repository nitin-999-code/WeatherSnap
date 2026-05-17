package com.example.weathersnap.presentation.create_report

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.core.util.FileUtils
import com.example.weathersnap.core.util.ImageCompressor
import com.example.weathersnap.domain.model.WeatherReport
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.domain.repository.ReportRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ReportRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        restoreState()
    }

    private fun restoreState() {
        val snapshotJson = savedStateHandle.get<String>("snapshotJson")
        val weatherSnapshot = if (snapshotJson != null) {
            Gson().fromJson(snapshotJson, WeatherSnapshot::class.java)
        } else null

        val notes = savedStateHandle.get<String>("notes") ?: ""
        val originalImagePath = savedStateHandle.get<String>("originalImagePath")
        val compressedImagePath = savedStateHandle.get<String>("compressedImagePath")
        val origSize = savedStateHandle.get<Long>("originalImageSizeBytes") ?: 0L
        val compSize = savedStateHandle.get<Long>("compressedImageSizeBytes") ?: 0L
        val isSaving = savedStateHandle.get<Boolean>("isSaving") ?: false

        _uiState.update {
            it.copy(
                weatherSnapshot = weatherSnapshot,
                notes = notes,
                originalImagePath = originalImagePath,
                compressedImagePath = compressedImagePath,
                originalImageSizeBytes = origSize,
                compressedImageSizeBytes = compSize,
                isSaving = isSaving
            )
        }
    }

    fun onNotesChange(notes: String) {
        savedStateHandle["notes"] = notes
        _uiState.update { it.copy(notes = notes) }
    }

    fun onImageCaptured(originalPath: String) {
        viewModelScope.launch {
            savedStateHandle["originalImagePath"] = originalPath
            _uiState.update { it.copy(originalImagePath = originalPath) }
            compressImage(originalPath)
        }
    }

    private suspend fun compressImage(originalPath: String) = withContext(Dispatchers.IO) {
        val file = File(originalPath)
        val result = ImageCompressor.compressImage(context, file)
        
        if (result != null) {
            withContext(Dispatchers.Main) {
                savedStateHandle["compressedImagePath"] = result.compressedFile.absolutePath
                savedStateHandle["originalImageSizeBytes"] = result.originalSizeBytes
                savedStateHandle["compressedImageSizeBytes"] = result.compressedSizeBytes
                
                _uiState.update {
                    it.copy(
                        compressedImagePath = result.compressedFile.absolutePath,
                        originalImageSizeBytes = result.originalSizeBytes,
                        compressedImageSizeBytes = result.compressedSizeBytes
                    )
                }
            }
        }
    }

    fun saveReport() {
        val state = _uiState.value
        if (state.isSaving || state.compressedImagePath == null || state.weatherSnapshot == null) return

        savedStateHandle["isSaving"] = true
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Copy to final location
                val tempCompressed = File(state.compressedImagePath)
                val finalFileName = "report_img_${UUID.randomUUID()}.jpg"
                val finalFile = FileUtils.getFinalImageFile(context, finalFileName)
                tempCompressed.copyTo(finalFile, overwrite = true)

                val report = WeatherReport(
                    cityName = state.weatherSnapshot.cityName,
                    country = state.weatherSnapshot.country,
                    condition = state.weatherSnapshot.condition,
                    temperature = state.weatherSnapshot.temperature,
                    humidity = state.weatherSnapshot.humidity,
                    windSpeed = state.weatherSnapshot.windSpeed,
                    pressure = state.weatherSnapshot.pressure,
                    notes = state.notes,
                    imagePath = finalFile.absolutePath,
                    originalImageSizeBytes = state.originalImageSizeBytes,
                    compressedImageSizeBytes = state.compressedImageSizeBytes,
                    savedAtMillis = System.currentTimeMillis()
                )
                
                repository.saveReport(report)

                // Cleanup temps
                state.originalImagePath?.let { File(it).delete() }
                tempCompressed.delete()
                
                withContext(Dispatchers.Main) {
                    savedStateHandle["isSaving"] = false
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    // Clear draft state after success to avoid reuse
                    savedStateHandle.remove<String>("originalImagePath")
                    savedStateHandle.remove<String>("compressedImagePath")
                    savedStateHandle.remove<String>("notes")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    savedStateHandle["isSaving"] = false
                    _uiState.update { it.copy(isSaving = false, error = e.localizedMessage) }
                }
            }
        }
    }
}
