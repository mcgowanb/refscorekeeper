package com.mcgowanb.projects.refereescorekeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun ScoreDisplayBox(
    text: String,
    verticalAlignment: Alignment
) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(verticalAlignment),
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            text = text
        )
    }
}