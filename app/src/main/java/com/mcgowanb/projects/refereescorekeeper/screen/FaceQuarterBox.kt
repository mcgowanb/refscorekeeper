package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.mcgowanb.projects.refereescorekeeper.model.BoxModel

@Composable
fun FaceQuarterBox(boxModel: BoxModel) {
    Box(
        modifier = boxModel.modifier,
        ) {
        FaceQuarter(model = boxModel.faceQuarterModel)
    }
}