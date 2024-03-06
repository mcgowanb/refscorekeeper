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
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .background(state.homeColor.copy(alpha = 0.3f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                totalPoints = state.totalHomeScore
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
                    onDoubleClick = { onAction(ScoreAction.SubtractHomeGoal) },
                    onLongClick = { onAction(ScoreAction.AddHomeGoal) }
                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractHomePoint) },
                    onLongClick = { onAction(ScoreAction.AddHomePoint) }
                )
            }
        }
        HorizontalSpacer(modifier = Modifier)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .background(state.awayColor.copy(alpha = 0.3f))
        ) {
            TotalPointsBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 20.dp),
                totalPoints = state.totalAwayScore
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
                    onDoubleClick = { onAction(ScoreAction.SubtractAwayGoal) },
                    onLongClick = { onAction(ScoreAction.AddAwayGoal) }
                )
                ScoreActionBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractAwayPoint) },
                    onLongClick = { onAction(ScoreAction.AddAwayPoint) }
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