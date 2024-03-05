package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
        ) {
            Row {
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractHomeGoal) },
                    onLongClick = { onAction(ScoreAction.AddHomeGoal) },
                    score = state.hGoals.toString(),
                    textAlign = TextAlign.Right,
                    textModifier = Modifier
                        .padding(end = 10.dp)
                        .wrapContentHeight(align = Alignment.Bottom)
                )
                VerticalSpacer(modifier = Modifier)
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractHomePoint) },
                    onLongClick = { onAction(ScoreAction.AddHomePoint) },
                    score = state.hPoints.toString(),
                    textAlign = TextAlign.Left,
                    textModifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentHeight(align = Alignment.Bottom)
                )
            }
        }
        HorizontalSpacer(modifier = Modifier)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
        ) {
            Row {
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractAwayGoal) },
                    onLongClick = { onAction(ScoreAction.AddAwayGoal) },
                    score = state.aGoals.toString(),
                    textAlign = TextAlign.Right,
                    textModifier = Modifier
                        .padding(end = 10.dp)
                        .wrapContentHeight(align = Alignment.Top)
                )
                VerticalSpacer(modifier = Modifier)
                ScoreBox(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractAwayPoint) },
                    onLongClick = { onAction(ScoreAction.AddAwayPoint) },
                    score = state.aPoints.toString(),
                    textAlign = TextAlign.Left,
                    textModifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentHeight(align = Alignment.Top)
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