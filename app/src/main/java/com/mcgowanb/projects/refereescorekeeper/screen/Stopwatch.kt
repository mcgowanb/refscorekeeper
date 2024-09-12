package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel

@Composable
fun Stopwatch(
    gameTimerViewModel: GameTimeViewModel
) {
    val remainingTime by gameTimerViewModel.formattedTime.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color.White)
            )
            Text(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                fontSize = 16.sp,
                text = remainingTime.toString(),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color.White)
            )
        }
    }

}