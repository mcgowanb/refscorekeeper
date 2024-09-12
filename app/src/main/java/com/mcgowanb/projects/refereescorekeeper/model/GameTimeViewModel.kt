package com.mcgowanb.projects.refereescorekeeper.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

class GameTimeViewModel : ViewModel() {
    private val _gameLengthInMinutes = 30
    private val _gameLengthInSeconds = _gameLengthInMinutes * 60

    private val _remainingTime = MutableStateFlow(_gameLengthInSeconds)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    private val _formattedTime = MutableStateFlow(formatTime(_gameLengthInSeconds))
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private val fileName = "timer_state.json"

    private lateinit var context: Context
    private lateinit var gson: Gson
    private var isInitialized = false

    fun init(context: Context, gson: Gson) {
        if (!isInitialized) {
            this.context = context
            this.gson = gson
            loadTimerState()
            isInitialized = true
        }
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
                saveTimerState()
            }
            _isRunning.value = false
            saveTimerState()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        saveTimerState()
    }

    fun resetTimer() {
        stopTimer()
        _remainingTime.value = _gameLengthInSeconds
        _formattedTime.value = formatTime(_gameLengthInSeconds)
        saveTimerState()
    }

    private fun formatTime(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun saveTimerState() {
        val timerState = TimerState(
            remainingTime = _remainingTime.value,
            isRunning = _isRunning.value,
            lastPausedTime = System.currentTimeMillis()
        )
        val jsonString = gson.toJson(timerState)
        val file = File(context.filesDir, fileName)
        file.writeText(jsonString)
    }

    private fun loadTimerState() {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            try {
                val jsonString = file.readText()
                val timerState = gson.fromJson(jsonString, TimerState::class.java)

                // Check if the loaded state is valid
                if (timerState.remainingTime > 0) {
                    _remainingTime.value = timerState.remainingTime
                    _formattedTime.value = formatTime(timerState.remainingTime)
                    _isRunning.value = timerState.isRunning

                    if (timerState.isRunning) {
                        val elapsedTime =
                            (System.currentTimeMillis() - timerState.lastPausedTime) / 1000
                        _remainingTime.value =
                            (timerState.remainingTime - elapsedTime).coerceAtLeast(0).toInt()
                        startTimer()
                    }
                } else {
                    // If the remaining time is 0 or negative, reset to default state
                    resetTimer()
                }
            } catch (e: JsonSyntaxException) {
                // If there's an error parsing the JSON, reset to default state
                resetTimer()
            } catch (e: Exception) {
                // If there's any other error reading the file, reset to default state
                resetTimer()
            }
        } else {
            // If no saved state exists, set to default state
            resetTimer()
        }
    }
}