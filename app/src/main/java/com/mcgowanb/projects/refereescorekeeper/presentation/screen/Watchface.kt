package com.mcgowanb.projects.refereescorekeeper.presentation.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun Watchface() {
    Column(
        modifier = Modifier
    ) {
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
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp)
                            .wrapContentHeight(align = Alignment.Bottom),
                        textAlign = TextAlign.Right,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        text = "20"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .wrapContentHeight(align = Alignment.Bottom),
                        textAlign = TextAlign.Left,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        text = "40"
                    )
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
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp)
                            .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Right,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        text = "60"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Left,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        text = "80"
                    )
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