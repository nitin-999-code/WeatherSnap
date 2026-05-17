package com.example.weathersnap.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedReportsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeReports().collect { reports ->
                _uiState.update { it.copy(reports = reports, isLoading = false) }
            }
        }
    }
}
