package com.mcgowanb.projects.refereescorekeeper.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Text

@Composable
fun ScoreQuarter(msg: String) {
    Text(
        modifier = Modifier,
        textAlign = TextAlign.Center,
        text = msg
    )
}

//@Preview
//@Composable
//fun ScreenComponentPreview() {
//    ScreenComponent("asfa")
//}