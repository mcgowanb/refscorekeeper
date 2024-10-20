package com.mcgowanb.projects.refereescorekeeper.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MatchReportViewModel(private val fileHandler: FileHandlerUtility?) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchReport())
    val uiState: StateFlow<MatchReport> = _uiState.asStateFlow()

    private val fileName = "match_report.json"

    init {
        viewModelScope.launch {
            val loadedState = loadFromFile(MatchReport::class.java)
            _uiState.value = loadedState ?: MatchReport()
        }
    }

    private fun <T> loadFromFile(clazz: Class<T>): T? {
        return fileHandler?.loadState(fileName, clazz)
    }

    private fun saveToFile() {
        viewModelScope.launch {
            fileHandler?.saveState(_uiState.value, fileName)
        }
    }

    fun resetReport() {
        _uiState.update {
            MatchReport()
        }
        saveToFile()
    }

    fun addEvent(event: String) {
        _uiState.update { currentState ->
            currentState.copy(events = currentState.events + event)
        }
        saveToFile()
    }
}