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
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.screen.Watchface
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme

@Composable
fun MainScreen(
    gameTimerViewModel: GameTimeViewModel,
    gameViewModel: GameViewModel
) {
    var showSettings by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectVerticalDragGestures { _, dragAmount ->
                when {
                    dragAmount < -50 && !showSettings && !gameTimerViewModel.isRunning.value ->
                        showSettings = true
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
                Watchface(gameViewModel, gameTimerViewModel)
                if (showSettings) {
                    SettingsOverlay(
                        showSettings = showSettings,
                        onClose = { showSettings = false }
                    )
                }
            }
        }
    }
}