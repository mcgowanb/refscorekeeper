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

    private var scoreEventCallback: ((Team, Int, Int, String) -> Unit)? = null


    private val fileName = "game_state.json"

    init {
        viewModelScope.launch {
            val loadedState = loadGameStateFromFile()
            _uiState.value = loadedState ?: GameState()
        }
    }

    fun setScoreEventCallback(callback: (Team, Int, Int, String) -> Unit) {
        scoreEventCallback = callback
    }

    fun getModel(): GameState {
        return _uiState.value
    }

    fun getHomeScore(): String {
        return _uiState.value.homeScore
    }

    fun getAwayScore(): String {
        return _uiState.value.awayScore
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
            ScoreAction.Reset -> resetGame()
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
        val scoreFormat = _uiState.value.homeScore + " : " + _uiState.value.awayScore
        scoreEventCallback?.invoke(team, points, goals, scoreFormat)
        saveGameStateToFile()
    }

    private fun resetGame() {
        _uiState.update { currentState ->
            GameState(
                showAdditionalInfo = currentState.showAdditionalInfo,
                periods = currentState.periods,
                showClock = currentState.showClock,
                hasExtraTime = currentState.hasExtraTime
            )
        }
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

    fun getPeriods(): Int {
        return _uiState.value.periods
    }

    fun getElapsedPeriods(): Int {
        return _uiState.value.elapsedPeriods
    }

    fun setElapsedPeriods(elapsed: Int) {
        _uiState.update { currentState ->
            currentState.copy(elapsedPeriods = elapsed)
        }
        saveGameStateToFile()
    }

    fun startGame() {
        _uiState.update { currentState ->
            currentState.copy(
                status = GameStatus.IN_PROGRESS,
                elapsedPeriods = currentState.elapsedPeriods + 1
            )
        }
        saveGameStateToFile()
    }

    fun toggleGameStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                status = if (currentState.status == GameStatus.IN_PROGRESS) GameStatus.PAUSED else GameStatus.IN_PROGRESS,
            )
        }
        saveGameStateToFile()
    }

    fun incrementElapsedPeriod() {
        _uiState.update { currentState ->
            if (currentState.elapsedPeriods < currentState.periods) {
                currentState.copy(elapsedPeriods = currentState.elapsedPeriods + 1)
            } else {
                currentState
            }
        }
        saveGameStateToFile()
    }

    fun setStatus(newStatus: GameStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = newStatus)
        }
        saveGameStateToFile()
    }

    fun toggleShowAdditionalInfo() {
        _uiState.update { it.copy(showAdditionalInfo = !it.showAdditionalInfo) }
            .also { saveGameStateToFile() }
    }

    fun toggleShowClock() {
        _uiState.update { it.copy(showClock = !it.showClock) }
            .also { saveGameStateToFile() }
    }

    fun toggleExtraTime() {
        _uiState.update { it.copy(hasExtraTime = !it.hasExtraTime) }
            .also { saveGameStateToFile() }
    }

    fun isGameComplete(): Boolean {
        return _uiState.value.status == GameStatus.COMPLETED
    }
}