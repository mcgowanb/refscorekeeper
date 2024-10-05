package com.mcgowanb.projects.refereescorekeeper.ui

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.dialog.ConfirmationDialog
import com.mcgowanb.projects.refereescorekeeper.ui.input.MinutePicker
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun GameActionOverlay(
    onClose: () -> Unit,
    gameViewModel: GameViewModel,
    gameTimerViewModel: GameTimeViewModel,
    vibrationUtility: VibrationUtility
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showNumberInput by remember { mutableStateOf(false) }
    var confirmationTitle by remember { mutableStateOf("") }
    var confirmationAction by remember { mutableStateOf({}) }

    val resetGame: () -> Unit = {
        gameViewModel.onAction(ScoreAction.Reset)
        gameTimerViewModel.resetTimer()
        vibrationUtility.vibrateMultiple(VibrationType.RESET, VibrationEffect.DEFAULT_AMPLITUDE)
        onClose()
    }

    val confirmNewGame: () -> Unit = {
        confirmationTitle = "New Game"
        confirmationAction = resetGame
        showConfirmationDialog = true
    }

    val resetClock: (Int) -> Unit = { selectedMinutes ->
        onClose()
        gameTimerViewModel.setPeriodLength(selectedMinutes)
        vibrationUtility.vibrateMultiple(VibrationType.RESET, VibrationEffect.DEFAULT_AMPLITUDE)
    }

    Scaffold(
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = rememberScalingLazyListState()) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = 40.dp,
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 40.dp
                )
            ) {
                item {
                    Chip(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        label = { Text("New Game") },
                        onClick = confirmNewGame,
                        colors = ChipDefaults.primaryChipColors(),
                        icon = { Icon(Icons.Rounded.Star, contentDescription = "New Game") }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    Chip(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        label = { Text("Period time: ${gameTimerViewModel.getPeriodLength()} mins") },
                        onClick = { showNumberInput = true },
                        colors = ChipDefaults.secondaryChipColors(),
                        icon = { Icon(Icons.Rounded.Timer, contentDescription = "Set period time") }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    Chip(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        label = { Text("Close") },
                        onClick = onClose,
                        colors = ChipDefaults.secondaryChipColors(
                            backgroundColor = WearColors.DismissRed
                        ),
                        icon = { Icon(Icons.Rounded.Close, contentDescription = "Close") }
                    )
                }
            }
        }
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            confirmationQuestion = confirmationTitle,
            onConfirm = {
                confirmationAction()
                showConfirmationDialog = false
            },
            onDismiss = { showConfirmationDialog = false }
        )
    }

    if (showNumberInput) {
        MinutePicker(
            initialMinutes = gameTimerViewModel.getPeriodLength(),
            range = 1..30,
            vibrationUtility = vibrationUtility,
            onConfirm = { selectedMinutes ->
                resetClock(selectedMinutes)
                showNumberInput = false
            },
            onDismiss = { showNumberInput = false }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun GameActionOverlayPreview() {
    GameActionOverlay(
        onClose = {},
        gameViewModel = GameViewModel(null),
        gameTimerViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null)
    )
}