package com.mcgowanb.projects.refereescorekeeper.model

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.S)
class GameTimeViewModel(
    private val fileHandler: FileHandlerUtility,
    private val vibrationUtility: VibrationUtility,
    private val soundUtility: SoundUtility
) : ViewModel() {
    private val _defaultGameLengthInMinutes = 30
    private var _mutableGameLength = _defaultGameLengthInMinutes
    private var _gameLengthInSeconds = _mutableGameLength * 60

    private val _remainingTime = MutableStateFlow(_gameLengthInSeconds)

    private val _formattedTime = MutableStateFlow(formatTime(_gameLengthInSeconds))
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadTimerState()
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
            onTimerFinished()
        }
    }

    private fun onTimerFinished() {
        resetTimer()
        vibrationUtility.vibrateMultiple(
            VibrationType.HALF_TIME,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
//        soundUtility.playSound(R.raw.whistle)
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        saveTimerState()
    }

    fun resetTimer() {
        stopTimer()
        _gameLengthInSeconds = _mutableGameLength * 60
        _remainingTime.value = _gameLengthInSeconds
        _formattedTime.value = formatTime(_gameLengthInSeconds)
        saveTimerState()

        vibrationUtility.vibrateMultiple(
            VibrationType.RESET,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
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
            lastPausedTime = System.currentTimeMillis(),
            defaultMinutes = _mutableGameLength
        )
        fileHandler.saveTimerState(timerState)
    }

    private fun loadTimerState() {
        val timerState = fileHandler.loadTimerState()
        if (timerState != null) {
            _mutableGameLength = timerState.defaultMinutes
            _gameLengthInSeconds = _mutableGameLength * 60

            if (timerState.remainingTime > 0 && timerState.remainingTime <= _gameLengthInSeconds) {
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
                initializeDefaultGame()
            }
        } else {
            initializeDefaultGame()
        }
    }

    private fun initializeDefaultGame() {
        _mutableGameLength = _defaultGameLengthInMinutes
        _gameLengthInSeconds = _mutableGameLength * 60
        _remainingTime.value = _gameLengthInSeconds
        _formattedTime.value = formatTime(_gameLengthInSeconds)
        _isRunning.value = false
    }

    fun getPeriodLength(): Int {
        return _mutableGameLength
    }

    fun setPeriodLength(periodLength: Int) {
        _mutableGameLength = periodLength
        _gameLengthInSeconds = _mutableGameLength * 60
        saveTimerState()
        resetTimer()
    }
}