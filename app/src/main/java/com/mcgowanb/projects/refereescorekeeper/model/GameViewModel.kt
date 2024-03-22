package com.mcgowanb.projects.refereescorekeeper.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val fileName = "game_state.json"

    init {
        loadGameStateFromFile()
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
        if (team == Team.HOME) {
            _uiState.update { currentState ->
                currentState.copy(
                    hGoals = _uiState.value.hGoals.plus(1)
                )
            }
            saveGameStateToFile(_uiState.value)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    aGoals = _uiState.value.aGoals.plus(1)
                )
            }
            saveGameStateToFile(_uiState.value)
        }
    }

    private fun addPoint(team: Team) {
        if (team == Team.HOME) {
            _uiState.update { currentState ->
                currentState.copy(
                    hPoints = _uiState.value.hPoints.plus(1)
                )
            }
            saveGameStateToFile(_uiState.value)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    aPoints = _uiState.value.aPoints.plus(1)
                )
            }
            saveGameStateToFile(_uiState.value)
        }
    }

    private fun subtractGoal(team: Team) {
        if (team == Team.HOME) {
            if (_uiState.value.hGoals > 0) {
                _uiState.update { currentState ->
                    currentState.copy(
                        hGoals = _uiState.value.hGoals.minus(1)
                    )
                }
            }
            saveGameStateToFile(_uiState.value)
        } else {
//            viewModelScope.launch {
            if (_uiState.value.aGoals > 0) {
                _uiState.update { currentState ->
                    currentState.copy(
                        aGoals = _uiState.value.aGoals.minus(1)
                    )
                }
            }
            saveGameStateToFile(_uiState.value)
//            }
        }
    }


    private fun subtractPoint(team: Team) {
        if (team == Team.HOME) {
            if (_uiState.value.hPoints > 0) {
                _uiState.update { currentState ->
                    currentState.copy(
                        hPoints = _uiState.value.hPoints.minus(1)
                    )
                }
                saveGameStateToFile(_uiState.value)
            }
        } else {
            if (_uiState.value.aPoints > 0) {
                _uiState.update { currentState ->
                    currentState.copy(
                        aPoints = _uiState.value.aPoints.minus(1)
                    )
                }
                saveGameStateToFile(_uiState.value)
            }
        }
    }

    private fun reset() {
        _uiState.value = GameState()
    }

    private fun loadGameStateFromFile() {
        val file = File(getApplication<Application>().filesDir, fileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val gameState = Gson().fromJson(jsonString, GameState::class.java)
            _uiState.value = gameState
        } else {
            reset()
        }
    }

    private fun saveGameStateToFile(gameState: GameState) {
        val jsonString = Gson().toJson(gameState)
        val file = File(getApplication<Application>().filesDir, fileName)
        file.writeText(jsonString)
    }
}