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
import com.mcgowanb.projects.refereescorekeeper.presentation.model.FaceQuarterModel

@Composable
fun FaceQuarter(model: FaceQuarterModel) {
    Text(
        modifier = model.modifier,
        textAlign = model.textAlign,
        color = model.colour,
        fontSize = model.fontSize,
        fontWeight = model.fontWeight,
        text = model.text
    )

}

@Preview
@Composable
fun FaceQuarterPreview() {
//    FaceQuarter("30")
}