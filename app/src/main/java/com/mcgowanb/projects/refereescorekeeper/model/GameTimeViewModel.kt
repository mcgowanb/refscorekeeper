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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.S)
class GameTimeViewModel(
    private val fileHandler: FileHandlerUtility?,
    private val vibrationUtility: VibrationUtility?,
    private val soundUtility: SoundUtility?
) : ViewModel() {
    private val _etLength = 10
    private val fileName = "timer_state.json"

    private val defaultPeriodMinutes = 30
    private val defaultSeconds = defaultPeriodMinutes * 60

    //    This and GameState should be merged
    private val initialState = TimerState(
        remainingTime = defaultPeriodMinutes,
        isRunning = false,
        formattedTime = formatTime(defaultSeconds),
        formattedElapsedTime = formatTime(0),
        isOvertime = false,
        overtimeSeconds = 0,
        currentPeriod = 0,
        defaultMinutes = defaultPeriodMinutes
    )

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<TimerState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var onPeriodEndCallback: (() -> Unit)? = null

    init {
        viewModelScope.launch {
            loadTimerState()
        }
    }

    fun toggleIsRunning() {
        when {
            _uiState.value.isOvertime -> resetTimer()
            _uiState.value.isRunning -> stopTimer()
            else -> startTimer()
        }
    }

    fun setOnPeriodEndCallback(callback: () -> Unit) {
        onPeriodEndCallback = callback
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        // Start first period if not started
        if (_uiState.value.currentPeriod == 0) {
            _uiState.update {
                it.copy(
                    currentPeriod = 1,
                    remainingTime = it.periodSeconds
                )
            }
        }

        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val initialRemainingTime = _uiState.value.remainingTime * 1000L
            val overtimeStartTime = startTime + initialRemainingTime

            while (true) {
                delay(1000)
                val currentTime = System.currentTimeMillis()

                if (!_uiState.value.isOvertime) {
                    val remainingMillis =
                        (initialRemainingTime - (currentTime - startTime)).coerceAtLeast(0)
                    val remainingSeconds = (remainingMillis / 1000.0).roundToInt()

                    _uiState.update { it.copy(remainingTime = remainingSeconds) }

                    if (remainingSeconds <= 0) {
                        handlePeriodEnd()
                    }
                } else {
                    val overtimeSeconds = ((currentTime - overtimeStartTime) / 1000.0).roundToInt()
                    _uiState.update { it.copy(overtimeSeconds = overtimeSeconds) }
                }

                updateDisplayTimes()
                saveTimerState()
            }
        }
    }

    private fun handlePeriodEnd() {
        _uiState.update { currentState ->
            when (currentState.currentPeriod) {
                1 -> currentState.copy(
                    currentPeriod = 2,
                    remainingTime = currentState.periodSeconds,
                    isOvertime = false,
                    overtimeSeconds = 0
                )

                2 -> currentState.copy(
                    isOvertime = true,
                    overtimeSeconds = 0
                )

                else -> currentState
            }
        }

        vibrationUtility?.vibrateMultiple(
            VibrationType.HALF_TIME,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
        onPeriodEndCallback?.invoke()
    }

    private fun updateDisplayTimes() {
        _uiState.update { currentState ->
            val timeToDisplay = if (currentState.isOvertime) {
                currentState.overtimeSeconds
            } else {
                currentState.remainingTime
            }
            val elapsedTime = currentState.periodSeconds - currentState.remainingTime

            currentState.copy(
                formattedTime = formatTime(timeToDisplay),
                formattedElapsedTime = formatTime(elapsedTime)
            )
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
        saveTimerState()
    }

    fun resetTimer(length: Int? = null) {
        stopTimer()
        _uiState.update {
            val remainingMinutes = length ?: it.defaultMinutes
            val remainingSeconds = remainingMinutes * 60
            it.copy(
                remainingTime = remainingSeconds,
                isRunning = false,
                formattedTime = formatTime(remainingSeconds),
                formattedElapsedTime = formatTime(0),
                isOvertime = false,
                defaultMinutes = remainingMinutes,
                overtimeSeconds = 0,
                currentPeriod = 0
            )
        }
        saveTimerState()
    }

    private fun formatTime(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun saveTimerState() {
        viewModelScope.launch {
            fileHandler?.saveState(_uiState.value, fileName)
        }
    }

    private fun loadTimerState() {
        val savedState = fileHandler?.loadState(fileName, TimerState::class.java)
        if (savedState != null) {
            if (!savedState.isOvertime && savedState.remainingTime > 0
                && savedState.remainingTime <= savedState.periodSeconds
            ) {
                _uiState.value = savedState
                if (savedState.isRunning) {
                    startTimer()
                }
            } else if (savedState.isOvertime) {
                _uiState.value = savedState
                if (savedState.isRunning) {
                    startTimer()
                }
            }
        }
        // If savedState is null, we keep the initialState that was set during initialization
    }

    fun getExtraTimeLength(): Int = _etLength

    fun setPeriodLength(periodLength: Int) {
        _uiState.update {
            it.copy(defaultMinutes = periodLength)
        }
        saveTimerState()
        resetTimer()
    }

    fun isRunning(): Boolean = _uiState.value.isRunning

    fun isOvertime(): Boolean = _uiState.value.isOvertime
}