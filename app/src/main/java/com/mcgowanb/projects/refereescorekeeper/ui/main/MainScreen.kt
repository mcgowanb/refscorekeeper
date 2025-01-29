package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Scaffold
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.model.MatchReportViewModel
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import com.mcgowanb.projects.refereescorekeeper.ui.menu.SettingsMenu
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    gameTimerViewModel: GameTimeViewModel,
    gameViewModel: GameViewModel,
    matchReportViewModel: MatchReportViewModel,
    vibrationUtility: VibrationUtility
) {
    var showMenu by remember { mutableStateOf(false) }
    val gameState by gameViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scalingLazyListState = rememberScalingLazyListState()

    val scrollToTop = {
        scope.launch {
            scalingLazyListState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = Modifier.verticalDragHandler(
            onDragUp = {
                showMenu = true
                scrollToTop()
            },
            onDragDown = {
                if (showMenu) {
                    showMenu = false
                    scrollToTop()
                }
            }
        )
    ) {
        Scaffold(timeText = { TimeInfo(gameState) }) {
            RefereeScoreKeeperTheme {
                Watchface(gameViewModel, gameTimerViewModel, vibrationUtility)
                SettingsMenu(
                    visible = showMenu,
                    onClose = { showMenu = false },
                    gameViewModel = gameViewModel,
                    gameTimeViewModel = gameTimerViewModel,
                    matchReportViewModel = matchReportViewModel,
                    vibrationUtility = vibrationUtility,
                    scalingLazyListState = scalingLazyListState
                )
            }
        }
    }
}

private fun Modifier.verticalDragHandler(
    onDragUp: () -> Unit,
    onDragDown: () -> Unit
): Modifier = pointerInput(Unit) {
    detectVerticalDragGestures { _, dragAmount ->
        when {
            dragAmount < -50 -> onDragUp()
            dragAmount > 50 -> onDragDown()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    val gvm = GameViewModel(null).apply {
        setPeriods(4)
        setElapsedPeriods(2)
        setStatus(GameStatus.H_T)
    }

    MainScreen(
        gameViewModel = gvm,
        gameTimerViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null),
        matchReportViewModel = MatchReportViewModel(null)
    )
}