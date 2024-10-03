package com.mcgowanb.projects.refereescorekeeper.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.enums.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        GameState()
    )

    private val fileName = "game_state.json"

    private lateinit var context: Context
    private lateinit var gson: Gson

    fun init(context: Context, gson: Gson) {
        this.context = context
        this.gson = gson
        viewModelScope.launch {
            val loadedState = loadGameStateFromFile()
            _uiState.value = loadedState
            // Force a re-emission of the state
            _uiState.value = _uiState.value.copy()
            Log.i("GameViewModel", "Initial state loaded and updated: $loadedState")
        }
    }

    fun onAction(action: ScoreAction) {
        when (action) {
            is ScoreAction.AddHomePoint -> addPoint(Team.HOME)
            is ScoreAction.SubtractHomePoint -> subtractPoint(Team.HOME)
            is ScoreAction.AddHomeGoal -> addGoal(Team.HOME)
            is ScoreAction.SubtractHomeGoal -> subtractGoal(Team.HOME)
            is ScoreAction.AddAwayPoint -> addPoint(Team.AWAY)
            is ScoreAction.SubtractAwayPoint -> subtractPoint(Team.AWAY)
            is ScoreAction.AddAwayGoal -> addGoal(Team.AWAY)
            is ScoreAction.SubtractAwayGoal -> subtractGoal(Team.AWAY)
            ScoreAction.Reset -> reset()
        }
    }

    private fun addGoal(team: Team) {
        _uiState.update { currentState ->
            when (team) {
                Team.HOME -> currentState.copy(hGoals = currentState.hGoals + 1)
                Team.AWAY -> currentState.copy(aGoals = currentState.aGoals + 1)
            }
        }
        saveGameStateToFile(_uiState.value)
    }

    private fun addPoint(team: Team) {
        _uiState.update { currentState ->
            when (team) {
                Team.HOME -> currentState.copy(hPoints = currentState.hPoints + 1)
                Team.AWAY -> currentState.copy(aPoints = currentState.aPoints + 1)
            }
        }
        saveGameStateToFile(_uiState.value)
    }

    private fun subtractGoal(team: Team) {
        _uiState.update { currentState ->
            when (team) {
                Team.HOME -> currentState.copy(hGoals = (currentState.hGoals - 1).coerceAtLeast(0))
                Team.AWAY -> currentState.copy(aGoals = (currentState.aGoals - 1).coerceAtLeast(0))
            }
        }
        saveGameStateToFile(_uiState.value)
    }

    private fun subtractPoint(team: Team) {
        _uiState.update { currentState ->
            when (team) {
                Team.HOME -> currentState.copy(hPoints = (currentState.hPoints - 1).coerceAtLeast(0))
                Team.AWAY -> currentState.copy(aPoints = (currentState.aPoints - 1).coerceAtLeast(0))
            }
        }
        saveGameStateToFile(_uiState.value)
    }

    private fun reset() {
        _uiState.value = GameState()
        saveGameStateToFile(_uiState.value)
    }

    private fun loadGameStateFromFile(): GameState {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            gson.fromJson(file.readText(), GameState::class.java)
        } else {
            GameState()
        }
    }

    private fun saveGameStateToFile(gameState: GameState) {
        viewModelScope.launch {
            val jsonString = gson.toJson(gameState)
            val file = File(context.filesDir, fileName)
            file.writeText(jsonString)
        }
    }

    fun getGameStatus(): GameStatus {
        return _uiState.value.status
    }
}