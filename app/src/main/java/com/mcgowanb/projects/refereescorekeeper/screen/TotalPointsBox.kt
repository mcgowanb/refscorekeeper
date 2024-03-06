package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun TotalPointsBox(
    modifier: Modifier,
    totalPoints: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = totalPoints,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}