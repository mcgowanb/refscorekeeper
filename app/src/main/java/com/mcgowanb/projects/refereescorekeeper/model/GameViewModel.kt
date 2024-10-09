package com.mcgowanb.projects.refereescorekeeper.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.enums.Team
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(private val fileHandler: FileHandlerUtility?) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val fileName = "game_state.json"

    init {
        viewModelScope.launch {
            val loadedState = loadGameStateFromFile()
            _uiState.value = loadedState ?: GameState()
        }
    }

    fun onAction(action: ScoreAction) {
        when (action) {
            is ScoreAction.AddHomePoint -> updateScore(Team.HOME, points = 1)
            is ScoreAction.SubtractHomePoint -> updateScore(Team.HOME, points = -1)
            is ScoreAction.AddHomeGoal -> updateScore(Team.HOME, goals = 1)
            is ScoreAction.SubtractHomeGoal -> updateScore(Team.HOME, goals = -1)
            is ScoreAction.AddAwayPoint -> updateScore(Team.AWAY, points = 1)
            is ScoreAction.SubtractAwayPoint -> updateScore(Team.AWAY, points = -1)
            is ScoreAction.AddAwayGoal -> updateScore(Team.AWAY, goals = 1)
            is ScoreAction.SubtractAwayGoal -> updateScore(Team.AWAY, goals = -1)
            ScoreAction.Reset -> resetGameScores()
        }
    }

    private fun updateScore(team: Team, points: Int = 0, goals: Int = 0) {
        _uiState.value = when (team) {
            Team.HOME -> _uiState.value.copy(
                hPoints = (_uiState.value.hPoints + points).coerceAtLeast(0),
                hGoals = (_uiState.value.hGoals + goals).coerceAtLeast(0)
            )

            Team.AWAY -> _uiState.value.copy(
                aPoints = (_uiState.value.aPoints + points).coerceAtLeast(0),
                aGoals = (_uiState.value.aGoals + goals).coerceAtLeast(0)
            )
        }
        saveGameStateToFile()
    }

    private fun resetGameScores() {
        _uiState.value = GameState()
        saveGameStateToFile()
    }

    private fun loadGameStateFromFile(): GameState? {
        return fileHandler?.loadState(fileName, GameState::class.java)
    }

    private fun saveGameStateToFile() {
        viewModelScope.launch {
            fileHandler?.saveState(_uiState.value, fileName)
        }
    }

    fun getGameStatus(): GameStatus = _uiState.value.status


    fun setPeriods(newPeriods: Int) {
        _uiState.update { currentState ->
            currentState.copy(periods = newPeriods)
        }
        saveGameStateToFile()
    }

    fun setElapsedPeriods(elapsed: Int) {
        _uiState.update { currentState ->
            currentState.copy(elapsedPeriods = elapsed)
        }
        saveGameStateToFile()
    }
}