package com.mcgowanb.projects.refereescorekeeper.ui

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import com.mcgowanb.projects.refereescorekeeper.ui.screen.Watchface
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@Composable
fun MainScreen(
    gameTimerViewModel: GameTimeViewModel,
    gameViewModel: GameViewModel,
    vibrationUtility: VibrationUtility
) {
    var showOverlay by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectVerticalDragGestures { _, dragAmount ->
                when {
                    dragAmount < -50 && !showOverlay && !gameTimerViewModel.isRunning.value -> {
                        showOverlay = true
                    }
                }
            }
        }
    ) {
        Scaffold(
            timeText = {
                TimeText(
                    timeTextStyle = TimeTextDefaults
                        .timeTextStyle(fontSize = 14.sp)
                )
            }
        ) {
            RefereeScoreKeeperTheme {
                Watchface(
                    gameViewModel,
                    gameTimerViewModel,
                    vibrationUtility
                )
                if (gameViewModel.getGameStatus() == GameStatus.NOT_STARTED && showOverlay) {
                    GameActionOverlay(
                        isVisible = showOverlay,
                        onClose = { showOverlay = false },
                        gameViewModel = gameViewModel,
                        gameTimerViewModel = gameTimerViewModel
                    )
//                    SettingsOverlay(
//                        showSettings = showSettings,
//                        onClose = { showSettings = false },
//                        listOf(
//                            Setting("periods", "Number of Periods", Icons.Rounded.Functions, 2),
//                            Setting(
//                                "periodDurationInMinutes",
//                                "Period Duration",
//                                Icons.Rounded.HourglassEmpty,
//                                30
//                            ),
//                            Setting("periodsPlayed", "Periods Played", Icons.Rounded.MoreTime, 0)
//                        )
//                    )
                }
            }
        }
    }
}