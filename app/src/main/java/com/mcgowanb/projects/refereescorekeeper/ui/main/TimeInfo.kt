package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.model.GameState

@Composable
@RequiresApi(Build.VERSION_CODES.S)
fun TimeInfo(
    gameState: GameState
) {
    if (gameState.showClock) {
        TimeText(
            timeTextStyle = TimeTextDefaults.timeTextStyle(fontSize = 12.sp),
            startLinearContent = {
                AdditionalInfoText(gameState.showAdditionalInfo) {
                    formatState(
                        gameState.status
                    )
                }
            },
            startCurvedContent = {
                if (gameState.showAdditionalInfo) {
                    curvedText(
                        text = formatState(gameState.status),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            },
            endLinearContent = {
                AdditionalInfoText(gameState.showAdditionalInfo) {
                    formatIntervals(
                        gameState
                    )
                }
            },
            endCurvedContent = {
                if (gameState.showAdditionalInfo) {
                    curvedText(
                        text = formatIntervals(gameState),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        )
    }
}

private fun formatIntervals(gameState: GameState): String =
    "${gameState.elapsedPeriods}/${gameState.periods}"

private fun formatState(status: GameStatus): String = when (status) {
    GameStatus.NOT_STARTED -> "N/S"
    GameStatus.IN_PROGRESS -> "I/P"
    GameStatus.PAUSED -> "P/S"
    GameStatus.H_T -> "H/T"
    GameStatus.F_T -> "F/T"
    else -> ""
}

@Composable
private fun AdditionalInfoText(showAdditionalInfo: Boolean, content: () -> String) {
    if (showAdditionalInfo) {
        Text(
            text = content(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun TimeInfoPreview() {
    val gameState = GameState(
        showAdditionalInfo = true,
        status = GameStatus.F_T,
        elapsedPeriods = 2,
        periods = 4
    )


    Scaffold(timeText = {
        TimeInfo(
            gameState = gameState
        )
    }) {
    }
}