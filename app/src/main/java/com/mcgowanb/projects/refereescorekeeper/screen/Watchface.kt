package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.model.GameState

@Composable
fun Watchface(
    state: GameState,
    onAction: (ScoreAction) -> Unit
) {
    Stopwatch()
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .padding(bottom = 5.dp)
                .background(state.homeColor.copy(alpha = 0.2f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                totalPoints = state.totalHomeScore,
                diff = state.homeDiff
            )
            ScoreDisplayBox(
                text = state.homeScore,
                verticalAlignment = Alignment.BottomCenter
            )
            Row {
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { onAction(ScoreAction.SubtractHomeGoal) },
                    addScore = { onAction(ScoreAction.AddHomeGoal) },
                    enableVibrate = state.hGoals != 0
                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { onAction(ScoreAction.SubtractHomePoint) },
                    addScore = { onAction(ScoreAction.AddHomePoint) },
                    enableVibrate = state.hPoints != 0
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .padding(top = 5.dp)
                .background(state.awayColor.copy(alpha = 0.2f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 20.dp),
                totalPoints = state.totalAwayScore,
                diff = state.awayDiff
            )
            ScoreDisplayBox(
                text = state.awayScore,
                verticalAlignment = Alignment.TopCenter
            )
            Row {
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { onAction(ScoreAction.SubtractAwayGoal) },
                    addScore = { onAction(ScoreAction.AddAwayGoal) },
                    enableVibrate = state.aGoals != 0

                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    subtractScore = { onAction(ScoreAction.SubtractAwayPoint) },
                    addScore = { onAction(ScoreAction.AddAwayPoint) },
                    enableVibrate = state.aPoints != 0

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