package com.mcgowanb.projects.refereescorekeeper.model

import androidx.lifecycle.ViewModel
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()


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
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    aGoals = _uiState.value.aGoals.plus(1)
                )
            }
        }
    }

    private fun addPoint(team: Team) {
        if (team == Team.HOME) {
            _uiState.update { currentState ->
                currentState.copy(
                    hPoints = _uiState.value.hPoints.plus(1)
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    aPoints = _uiState.value.aPoints.plus(1)
                )
            }
        }
    }

    private fun subtractGoal(team: Team) {
        if (team == Team.HOME) {
            if (_uiState.value.hGoals > 0)
                _uiState.update { currentState ->
                    currentState.copy(
                        hGoals = _uiState.value.hGoals.minus(1)
                    )
                }
        } else {
            if (_uiState.value.aGoals > 0)
                _uiState.update { currentState ->
                    currentState.copy(
                        aGoals = _uiState.value.hGoals.minus(1)
                    )
                }
        }
    }


    private fun subtractPoint(team: Team) {
        if (team == Team.HOME) {
            if (_uiState.value.hPoints > 0)
                _uiState.update { currentState ->
                    currentState.copy(
                        hPoints = _uiState.value.hPoints.minus(1)
                    )
                }
        } else {
            if (_uiState.value.aPoints > 0)
                _uiState.update { currentState ->
                    currentState.copy(
                        aPoints = _uiState.value.aPoints.minus(1)
                    )
                }
        }
    }

    private fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                hPoints = 0,
                hGoals = 0,
                aPoints = 0,
                aGoals = 0
            )

        }
    }
}