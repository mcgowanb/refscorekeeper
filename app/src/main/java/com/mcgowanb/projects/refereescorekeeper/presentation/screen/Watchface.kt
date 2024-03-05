package com.mcgowanb.projects.refereescorekeeper.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.presentation.model.BoxModel
import com.mcgowanb.projects.refereescorekeeper.presentation.model.FaceQuarterModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Watchface() {
    var points by remember {
        mutableStateOf(0)
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
        ) {
            Row {
                var bm = BoxModel(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                        .combinedClickable(
                            onClick = {
                            },
                            onLongClick = {
//                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                points++
                            },
                            onDoubleClick = {
                                points--
                            }
                        ),
                    faceQuarterModel = FaceQuarterModel(
                        text = points.toString(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp)
                            .wrapContentHeight(align = Alignment.Bottom),
                        textAlign = TextAlign.Right
                    )
                )
                FaceQuarterBox(boxModel = bm)

                Spacer(modifier = Modifier
                    .width(2.dp)
                    .fillMaxSize()
                )
                Spacer(modifier = Modifier
                    .width(1.dp)
                    .fillMaxSize()
                    .background(Color.White)
                )
                Spacer(modifier = Modifier
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
                Spacer(modifier = Modifier
                    .width(5.dp)
                    .fillMaxSize()
                    .background(Color.White))
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
    Watchface()

}