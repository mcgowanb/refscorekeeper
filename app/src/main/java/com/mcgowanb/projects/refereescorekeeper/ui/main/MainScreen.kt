package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import com.mcgowanb.projects.refereescorekeeper.ui.GameActionOverlay
import com.mcgowanb.projects.refereescorekeeper.ui.Watchface
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    gameTimerViewModel: GameTimeViewModel,
    gameViewModel: GameViewModel,
    vibrationUtility: VibrationUtility
) {
    var showOverlay by remember { mutableStateOf(false) }
    val gameState by gameViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val scalingLazyListState = rememberScalingLazyListState()

    fun formatIntervals(): String {
        return "%s/%s".format(gameState.elapsedPeriods, gameState.periods)
    }

    fun formatState(): String {
        return when (gameState.status) {
            GameStatus.NOT_STARTED -> "N/S"
            GameStatus.IN_PROGRESS -> "I/P"
            GameStatus.PAUSED -> "P/S"
            GameStatus.H_T -> "H/T"
            GameStatus.F_T -> "F/T"
            else -> ""
        }
    }

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
                AnimatedVisibility(
                    visible = !scalingLazyListState.isScrollInProgress && scalingLazyListState.centerItemIndex == 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ){
                TimeText(
                        timeTextStyle = TimeTextDefaults.timeTextStyle(fontSize = 12.sp),
                        endLinearContent = {
                            if (gameState.showAdditionalInfo) {
                                Text(
                                    text = formatIntervals(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        },
                        endCurvedContent = {
                            if (gameState.showAdditionalInfo) {
                                curvedText(
                                    text = formatIntervals(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        },
                        startLinearContent = {
                            if (gameState.showAdditionalInfo) {
                                Text(
                                    text = formatState(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        },
                        startCurvedContent = {
                            if (gameState.showAdditionalInfo) {
                                curvedText(
                                    text = formatState(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        },
                    )
                }
            }
        ) {
            RefereeScoreKeeperTheme {
                Watchface(
                    gameViewModel,
                    gameTimerViewModel,
                    vibrationUtility
                )
                AnimatedVisibility(
                    visible = showOverlay,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    GameActionOverlay(
                        onClose = {
                            showOverlay = !showOverlay
                            scope.launch {
                                scalingLazyListState.animateScrollToItem(0)
                            }
                        },
                        gameViewModel = gameViewModel,
                        gameTimeViewModel = gameTimerViewModel,
                        vibrationUtility = vibrationUtility,
                        scalingLazyListState = scalingLazyListState
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    val gvm = GameViewModel(null)
    gvm.setPeriods(4)
    gvm.setElapsedPeriods(2)
    gvm.setStatus(GameStatus.H_T)
    gvm.showAdditionalInfo(true)

    MainScreen(
        gameViewModel = gvm,
        gameTimerViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null)
    )

}