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
    private val fileHandler: FileHandlerUtility?,
    private val vibrationUtility: VibrationUtility?,
    private val soundUtility: SoundUtility?
) : ViewModel() {
    private val _defaultGameLengthInMinutes = 30
    private var _mutableGameLength = _defaultGameLengthInMinutes
    private var _gameLengthInSeconds = _mutableGameLength * 60
    private var _etLength = 10

    private val _remainingTime = MutableStateFlow(_gameLengthInSeconds)

    private val _formattedTime = MutableStateFlow(formatTime(_gameLengthInSeconds))
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    private val _fileName = "timer_state.json"

    private val _isOvertime = MutableStateFlow(false)
    val isOvertime: StateFlow<Boolean> = _isOvertime.asStateFlow()

    private val _overtimeSeconds = MutableStateFlow(0)

    private var onPeriodEndCallback: (() -> Unit)? = null


    init {
        loadTimerState()
    }

    fun toggleIsRunning() {
        if (_isOvertime.value) {
            resetTimer()
        } else {
            if (_isRunning.value) {
                stopTimer()
            } else {
                startTimer()
            }
        }
    }

    fun zeroClock() {
        stopTimer()
        _formattedTime.value = formatTime(0)
        saveTimerState()
    }

    fun setOnPeriodEndCallback(callback: () -> Unit) {
        onPeriodEndCallback = callback
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val initialRemainingTime = _remainingTime.value * 1000L // Convert to milliseconds
            val overtimeStartTime = startTime + initialRemainingTime
            while (true) {
                delay(100) // Update every 100ms for a balance between smoothness and efficiency
                val currentTime = System.currentTimeMillis()
                if (!_isOvertime.value) {
                    val remainingMillis =
                        (initialRemainingTime - (currentTime - startTime)).coerceAtLeast(0)
                    _remainingTime.value = (remainingMillis / 1000.0).roundToInt()
                    if (_remainingTime.value <= 0) {
                        _isOvertime.value = true
                        onRegularTimeFinished()
                        onPeriodEndCallback?.invoke()
                    }
                } else {
                    _overtimeSeconds.value =
                        ((currentTime - overtimeStartTime) / 1000.0).roundToInt()
                }
                _formattedTime.value =
                    formatTime(if (_isOvertime.value) _overtimeSeconds.value else _remainingTime.value)
                saveTimerState()
            }
        }
    }

    private fun onRegularTimeFinished() {
        vibrationUtility?.vibrateMultiple(
            VibrationType.HALF_TIME,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
        // soundUtility?.playSound(R.raw.whistle)
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
        _isOvertime.value = false
        _overtimeSeconds.value = 0
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
            lastPausedTime = System.currentTimeMillis(),
            defaultMinutes = _mutableGameLength,
            isOvertime = _isOvertime.value,
            overtimeSeconds = _overtimeSeconds.value
        )
        fileHandler?.saveState(timerState, _fileName)
    }

    private fun loadTimerState() {
        val timerState = fileHandler?.loadState(_fileName, TimerState::class.java)
        if (timerState != null) {
            _mutableGameLength = timerState.defaultMinutes
            _gameLengthInSeconds = _mutableGameLength * 60
            _isOvertime.value = timerState.isOvertime
            _overtimeSeconds.value = timerState.overtimeSeconds

            if (!_isOvertime.value && timerState.remainingTime > 0 && timerState.remainingTime <= _gameLengthInSeconds) {
                _remainingTime.value = timerState.remainingTime
                _formattedTime.value = formatTime(timerState.remainingTime)
            } else if (_isOvertime.value) {
                _formattedTime.value = formatTime(_overtimeSeconds.value)
            } else {
                initializeDefaultGame()
            }

            _isRunning.value = timerState.isRunning
            if (timerState.isRunning) {
                startTimer()
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

    fun getExtraTimeLength(): Int {
        return _etLength
    }

    fun setPeriodLength(periodLength: Int) {
        _mutableGameLength = periodLength
        _gameLengthInSeconds = _mutableGameLength * 60
        saveTimerState()
        resetTimer()
    }
}