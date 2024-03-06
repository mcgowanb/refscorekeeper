package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun TotalPointsBox(
    modifier: Modifier,
    totalPoints: String,
    diff: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .padding(end = 2.dp),
            text = totalPoints,
            textAlign = TextAlign.Right,
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .padding(start = 2.dp),
            text = diff,
            textAlign = TextAlign.Left,
            fontSize = 8.sp
        )
    }
}