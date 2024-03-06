package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.Team

class RefScoreViewModel : ViewModel() {
    var state by mutableStateOf(GameState())
        private set

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
            is ScoreAction.Reset -> state = GameState()
        }
    }

    private fun addGoal(team: Team) {
        state = if (team == Team.HOME) {
            state.copy(
                hGoals = state.hGoals + 1
            )
        } else {
            state.copy(
                aGoals = state.aGoals + 1
            )
        }
    }

    private fun subtractGoal(team: Team) {
        if (team == Team.HOME) {
            if (state.hGoals > 0) {
                state = state.copy(
                    hGoals = state.hGoals - 1
                )
            }
        } else {
            if (state.aGoals > 0) {
                state = state.copy(
                    aGoals = state.aGoals - 1
                )
            }
        }
    }


    private fun addPoint(team: Team) {
        state = if (team == Team.HOME) {
            state.copy(
                hPoints = state.hPoints + 1
            )
        } else {
            state.copy(
                aPoints = state.aPoints + 1
            )
        }
    }

    private fun subtractPoint(team: Team) {
        if (team == Team.HOME) {
            if (state.hPoints > 0) {
                state = state.copy(
                    hPoints = state.hPoints - 1
                )
            }
        } else {
            if (state.aPoints > 0) {
                state = state.copy(
                    aPoints = state.aPoints + 1
                )
            }
        }
    }
}