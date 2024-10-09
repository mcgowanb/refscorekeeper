package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Stopwatch(
    gameTimeViewModel: GameTimeViewModel,
    gameViewModel: GameViewModel
) {
    val remainingTime by gameTimeViewModel.formattedTime.collectAsState()
    val isOvertime by gameTimeViewModel.isOvertime.collectAsState()
    val gameState by gameViewModel.uiState.collectAsState()

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
            repeat(2) { index ->
                Spacer(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(4.dp)
                        .align(Alignment.CenterVertically)
                        .background(
                            when {
                                gameState.periods == 2 && gameState.elapsedPeriods > 0 -> WearColors.ConfirmGreen
                                gameState.periods == 4 && index < gameState.elapsedPeriods -> WearColors.ConfirmGreen
                                else -> WearColors.White
                            }
                        )
                )
            }
            Text(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                fontSize = 16.sp,
                text = remainingTime,
                textAlign = TextAlign.Center,
                color = if (isOvertime) WearColors.DismissRed else Color.White
            )
            repeat(2) { index ->
                Spacer(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(4.dp)
                        .align(Alignment.CenterVertically)
                        .background(
                            when {
                                gameState.periods == 2 && gameState.elapsedPeriods == 2 -> WearColors.ConfirmGreen
                                gameState.periods == 4 && index + 2 < gameState.elapsedPeriods -> WearColors.ConfirmGreen
                                else -> WearColors.White
                            }
                        )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun StopWatchPreview() {
    val gvm = GameViewModel(null)
    gvm.setPeriods(2)
    gvm.setElapsedPeriods(2)
    Stopwatch(
        gameTimeViewModel = GameTimeViewModel(
            null, null, null
        ),
        gameViewModel = gvm
    )
}