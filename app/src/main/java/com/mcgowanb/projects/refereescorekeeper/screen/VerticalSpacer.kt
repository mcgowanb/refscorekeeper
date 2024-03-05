package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacer(modifier: Modifier) {
    Spacer(
        modifier = Modifier
            .width(2.dp)
            .fillMaxSize()
    )
    Spacer(
        modifier = Modifier
            .width(1.dp)
            .fillMaxSize()
            .background(Color.White)
    )
    Spacer(
        modifier = Modifier
            .width(2.dp)
            .fillMaxSize()
    )
}