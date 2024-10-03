package com.mcgowanb.projects.refereescorekeeper.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@Composable
fun Watchface(
    gameViewModel: GameViewModel,
    gameTimerViewModel: GameTimeViewModel,
    vibrationUtility: VibrationUtility
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Stopwatch(
        gameTimerViewModel = gameTimerViewModel
    )
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
                    shouldVibrate = gameUiState.hGoals != 0,
                    vibrationUtility
                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractHomePoint) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddHomePoint) },
                    shouldVibrate = gameUiState.hPoints != 0,
                    vibrationUtility
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
                    shouldVibrate = gameUiState.aGoals != 0,
                    vibrationUtility

                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { gameViewModel.onAction(ScoreAction.SubtractAwayPoint) },
                    addScore = { gameViewModel.onAction(ScoreAction.AddAwayPoint) },
                    shouldVibrate = gameUiState.aPoints != 0,
                    vibrationUtility
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun WatchfacePreview() {
    Watchface(
        gameViewModel = GameViewModel(),
        gameTimerViewModel = GameTimeViewModel(),
        vibrationUtility = VibrationUtility(null)
    )
}