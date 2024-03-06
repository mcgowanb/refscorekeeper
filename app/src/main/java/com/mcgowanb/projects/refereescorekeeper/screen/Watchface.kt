package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
        ) {
            ScoreDisplay(
                text = state.homeScore,
                verticalAlignment = Alignment.BottomCenter
            )
            Row {
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractHomeGoal) },
                    onLongClick = { onAction(ScoreAction.AddHomeGoal) }
                )
                ScoreBox(
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
        ) {
            ScoreDisplay(
                text = state.awayScore,
                verticalAlignment = Alignment.TopCenter
            )
            Row {
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractAwayGoal) },
                    onLongClick = { onAction(ScoreAction.AddAwayGoal) }
                )
                ScoreBox(
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