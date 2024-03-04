package com.mcgowanb.projects.refereescorekeeper.presentation.model

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class FaceQuarterModel(
    var text: String,
    var textAlign: TextAlign,
    var modifier: Modifier,
    var colour: Color = Color.White,
    var fontSize: TextUnit = 50.sp,
    var fontWeight: FontWeight = FontWeight.Bold
)