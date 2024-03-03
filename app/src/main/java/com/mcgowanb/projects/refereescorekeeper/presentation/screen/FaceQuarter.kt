package com.mcgowanb.projects.refereescorekeeper.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun FaceQuarter(msg: String) {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 10.dp)
            .wrapContentHeight(align = Alignment.Bottom),
        textAlign = TextAlign.Right,
        color = Color.White,
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        text = msg
    )

}

@Preview
@Composable
fun FaceQuarterPreview() {
    FaceQuarter("30")
}