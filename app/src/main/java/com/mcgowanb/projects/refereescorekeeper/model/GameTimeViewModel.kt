package com.mcgowanb.projects.refereescorekeeper.model

import android.content.Context
import android.os.Build
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.S)
class GameTimeViewModel : ViewModel() {
    private val _defaualtGameLengthInMinutes = 30
    private var _mutableGameLength = _defaualtGameLengthInMinutes
    private var _gameLengthInSeconds = _mutableGameLength * 60

    private val _remainingTime = MutableStateFlow(_gameLengthInSeconds)

    private val _formattedTime = MutableStateFlow(formatTime(_gameLengthInSeconds))
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private val fileName = "timer_state.json"

    private lateinit var context: Context
    private lateinit var vibrationUtility: VibrationUtility
    private lateinit var vibratorManager: VibratorManager
    private lateinit var gson: Gson
    private lateinit var soundUtility: SoundUtility
    private var isInitialized = false

    fun init(
        context: Context,
        gson: Gson,
        vibrationUtility: VibrationUtility,
        vibrationManager: VibratorManager
    ) {
        if (!isInitialized) {
            this.context = context
            this.gson = gson
            this.vibrationUtility = vibrationUtility
            this.vibratorManager = vibrationManager
            loadTimerState()
            soundUtility = SoundUtility(context)
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
            onTimerFinished()
        }
    }

    private fun onTimerFinished() {
        resetTimer()
        vibratorManager.defaultVibrator.vibrate(
            vibrationUtility.getMultiShot(VibrationType.HALF_TIME)
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
        vibratorManager.defaultVibrator.vibrate(
            vibrationUtility.getMultiShot(VibrationType.RESET)
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

                // Always load the persisted game length
                _mutableGameLength = timerState.defaultMinutes
                _gameLengthInSeconds = _mutableGameLength * 60

                // Check if the loaded state is valid
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
                    // If the remaining time is invalid, reset to the full game length
                    _remainingTime.value = _gameLengthInSeconds
                    _formattedTime.value = formatTime(_gameLengthInSeconds)
                    _isRunning.value = false
                }
            } catch (e: JsonSyntaxException) {
                // If there's an error parsing the JSON, reset to default state
                initializeDefaultGame()
            } catch (e: Exception) {
                // If there's any other error reading the file, reset to default state
                initializeDefaultGame()
            }
        } else {
            // If no saved state exists, set to default state
            initializeDefaultGame()
        }
    }

    private fun initializeDefaultGame() {
        _mutableGameLength = _defaualtGameLengthInMinutes
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