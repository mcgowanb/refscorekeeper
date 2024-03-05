package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.model.FaceQuarterModel
import com.mcgowanb.projects.refereescorekeeper.model.GameState

@OptIn(ExperimentalFoundationApi::class)
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
                Fqb2(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    onDoubleClick = { onAction(ScoreAction.SubtractHomePoint) },
                    onLongClick = { onAction(ScoreAction.AddHomePoint) },
                    score = state.hPoints.toString()
                )
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxSize()
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxSize()
                        .background(Color.White)
                )
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                ) {
                    var fqm = FaceQuarterModel(
                        text = "4",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .wrapContentHeight(align = Alignment.Bottom),
                        textAlign = TextAlign.Left
                    )
                    FaceQuarter(fqm)
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                ) {
                    var fqm = FaceQuarterModel(
                        text = "6",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp)
                            .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Right
                    )
                    FaceQuarter(fqm)
                }
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxSize()
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                ) {
                    var fqm = FaceQuarterModel(
                        text = "8",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Left
                    )
                    FaceQuarter(fqm)
                }
            }
        }
    }
}

@Preview
@Composable
private fun WatchfacePreview() {
//    Watchface()

}