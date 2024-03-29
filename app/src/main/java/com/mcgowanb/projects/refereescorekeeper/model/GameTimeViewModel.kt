package com.mcgowanb.projects.refereescorekeeper.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcgowanb.projects.refereescorekeeper.enums.TimerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class GameTimeViewModel : ViewModel() {
    private val _remainingTime = MutableStateFlow(1800000L)
    private val _timerState = MutableStateFlow(TimerState.RESET)
    val timerState = _timerState.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("mm:ss:SS")

    val stopWatchText = _remainingTime.map { millis ->
        LocalTime
            .ofNanoOfDay(millis * 1_000_000)
            .format(formatter)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "30:00:00"
    )

    fun init() {
        _timerState.flatMapLatest { timerState ->
            getTimerFlow(
                isRunning = timerState.equals(TimerState.RUNNING)
            )
        }
            .onEach { timeDiff ->
                _remainingTime.update { it - timeDiff }
            }
            .launchIn(viewModelScope)
    }

    fun toggleIsRunning() {
        when (timerState.value) {
            TimerState.RUNNING -> _timerState.update { TimerState.PAUSED }
            TimerState.PAUSED,
            TimerState.RESET -> _timerState.update { TimerState.RUNNING }

        }
    }

    fun resetTimer() {
        _timerState.update { TimerState.RESET }
        _remainingTime.update { 0L }
    }

    private fun getTimerFlow(isRunning: Boolean): Flow<Long> {
        return flow {
            var startTimeMillis = System.currentTimeMillis()

            while (isRunning) {
                val currentMillis = System.currentTimeMillis()
                val timeDiff = if (currentMillis > startTimeMillis) {
                    currentMillis - startTimeMillis
                } else {
                    0L
                }
                emit(timeDiff)
                startTimeMillis = System.currentTimeMillis()
                delay(10L)
            }
        }
    }
}