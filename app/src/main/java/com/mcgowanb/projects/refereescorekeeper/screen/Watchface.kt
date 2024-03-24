package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel

@Composable
fun Watchface(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Stopwatch()
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .padding(bottom = 5.dp)
                .background(gameUiState.homeColor.copy(alpha = 0.2f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                totalPoints = gameUiState.totalHomeScore,
                diff = gameUiState.homeDiff
            )
            ScoreDisplayBox(
                text = gameUiState.homeScore,
                verticalAlignment = Alignment.BottomCenter
            )
            Row {
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractHomeGoal) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddHomeGoal) },
                    shouldVibrate = gameUiState.hGoals != 0
                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractHomePoint) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddHomePoint) },
                    shouldVibrate = gameUiState.hPoints != 0
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .padding(top = 5.dp)
                .background(gameUiState.awayColor.copy(alpha = 0.2f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 20.dp),
                totalPoints = gameUiState.totalAwayScore,
                diff = gameUiState.awayDiff
            )
            ScoreDisplayBox(
                text = gameUiState.awayScore,
                verticalAlignment = Alignment.TopCenter
            )
            Row {
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractAwayGoal) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddAwayGoal) },
                    shouldVibrate = gameUiState.aGoals != 0

                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractAwayPoint) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddAwayPoint) },
                    shouldVibrate = gameUiState.aPoints != 0

                )
            }
        }
    }
}

@Preview
@Composable
private fun WatchfacePreview() {
//    Watchface()

}