package com.mcgowanb.projects.refereescorekeeper.model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameTimeViewModel : ViewModel() {
    private val _remainingTime = MutableStateFlow(30 * 60) // 30 minutes in seconds
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    private val _formattedTime = MutableStateFlow("30:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    fun init() {
        // Initialize if needed
    }

    fun toggleIsRunning() {
        if (_isRunning.value) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val initialRemainingTime = _remainingTime.value * 1000L // Convert to milliseconds
            while (_remainingTime.value > 0) {
                delay(100) // Update every 100ms for a balance between smoothness and efficiency
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingMillis = (initialRemainingTime - elapsedTime).coerceAtLeast(0)
                _remainingTime.value = (remainingMillis / 1000.0).roundToInt()
                _formattedTime.value = formatTime(_remainingTime.value)
            }
            _isRunning.value = false
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun resetTimer() {
        stopTimer()
        _remainingTime.value = 30 * 60 // Reset to 30 minutes
        _formattedTime.value = "30:00"
    }

    private fun formatTime(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}