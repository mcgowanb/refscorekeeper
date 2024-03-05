package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.model.FaceQuarterModel

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