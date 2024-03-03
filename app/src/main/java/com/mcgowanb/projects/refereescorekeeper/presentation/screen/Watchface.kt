package com.mcgowanb.projects.refereescorekeeper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
                        .background(Color.Cyan)
                        .fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .background(Color.Green)
                        .fillMaxHeight()
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
//                .background(Color.Red)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .background(Color.Red)
                        .fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .background(Color.Magenta)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Preview
@Composable
private fun WatchfacePreview() {
    Watchface()

}