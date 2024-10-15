package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Scaffold
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

    Box(modifier = Modifier.verticalDragHandler { showOverlay = true }) {
        Scaffold(timeText = { TimeInfo(gameState) }) {
            RefereeScoreKeeperTheme {
                Watchface(gameViewModel, gameTimerViewModel, vibrationUtility)
                GameActionOverlayWrapper(
                    showOverlay,
                    onClose = {
                        showOverlay = false
                        scope.launch { scalingLazyListState.animateScrollToItem(0) }
                    },
                    gameViewModel,
                    gameTimerViewModel,
                    vibrationUtility,
                    scalingLazyListState
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun BoxScope.GameActionOverlayWrapper(
    showOverlay: Boolean,
    onClose: () -> Unit,
    gameViewModel: GameViewModel,
    gameTimeViewModel: GameTimeViewModel,
    vibrationUtility: VibrationUtility,
    scalingLazyListState: ScalingLazyListState
) {
    AnimatedVisibility(
        visible = showOverlay,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        GameActionOverlay(
            onClose = onClose,
            gameViewModel = gameViewModel,
            gameTimeViewModel = gameTimeViewModel,
            vibrationUtility = vibrationUtility,
            scalingLazyListState = scalingLazyListState
        )
    }
}

private fun Modifier.verticalDragHandler(onDragUp: () -> Unit): Modifier = pointerInput(Unit) {
    detectVerticalDragGestures { _, dragAmount ->
        if (dragAmount < -50) onDragUp()
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
        showAdditionalInfo(true)
    }

    MainScreen(
        gameViewModel = gvm,
        gameTimerViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null)
    )
}