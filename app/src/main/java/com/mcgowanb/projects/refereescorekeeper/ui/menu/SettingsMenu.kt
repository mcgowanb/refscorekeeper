package com.mcgowanb.projects.refereescorekeeper.ui.menu

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.RotateLeft
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.DialogState
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.model.MatchReportViewModel
import com.mcgowanb.projects.refereescorekeeper.model.Separator
import com.mcgowanb.projects.refereescorekeeper.ui.animation.SlideUpVertically
import com.mcgowanb.projects.refereescorekeeper.ui.menu.button.MenuItem
import com.mcgowanb.projects.refereescorekeeper.ui.menu.button.ToggleButton
import com.mcgowanb.projects.refereescorekeeper.ui.menu.dialog.ConfirmationDialog
import com.mcgowanb.projects.refereescorekeeper.ui.menu.input.MinutePicker
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsMenu(
    onClose: () -> Unit,
    gameViewModel: GameViewModel,
    gameTimeViewModel: GameTimeViewModel,
    matchReportViewModel: MatchReportViewModel,
    vibrationUtility: VibrationUtility,
    scalingLazyListState: ScalingLazyListState,
    visible: Boolean
) {
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.Hidden) }
    val gameState by gameViewModel.uiState.collectAsState()
    val matchReportState by matchReportViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val chipModifier = remember {
        Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 2.dp)
    }

    val resetGame = remember {
        { gameLength: Int? ->
            gameViewModel.onAction(ScoreAction.Reset)
            matchReportViewModel.resetReport()
            gameTimeViewModel.resetTimer(gameLength)
            scope.launch {
                vibrationUtility.vibrateMultiple(VibrationType.CRESCENDO)
            }
            onClose()
        }
    }

    SlideUpVertically(visible = visible) {
        Scaffold(
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = scalingLazyListState,
                    contentPadding = PaddingValues(
                        top = 40.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 40.dp
                    )
                ) {
                    // Game Report (only shown when game is finished)
                    if (gameState.status == GameStatus.F_T) {
                        item {
                            MenuItem(
                                label = "Game Report",
                                onClick = { dialogState = DialogState.Report },
                                icon = Icons.AutoMirrored.Rounded.ReceiptLong,
                                modifier = chipModifier,
                                visible = true
                            )
                        }

                        // New Game
                        item {
                            MenuItem(
                                label = "New Game",
                                onClick = {
                                    dialogState = DialogState.Confirmation(
                                        title = "New Game?",
                                        onConfirm = { resetGame(null) }
                                    )
                                },
                                icon = Icons.Rounded.RestartAlt,
                                modifier = chipModifier,
                                visible = true
                            )
                        }

                        // Minutes
                        item {
                            MenuItem(
                                label = "Minutes",
                                value = "${gameTimeViewModel.getPeriodLength() / 60}",
                                onClick = {
                                    dialogState = DialogState.NumberPicker(
                                        title = "Minutes",
                                        initialValue = gameTimeViewModel.getPeriodLength() / 60,
                                        range = 1..30,
                                        onConfirm = { minutes ->
                                            onClose()
                                            gameTimeViewModel.setPeriodLength(minutes * 60)
                                        }
                                    )
                                },
                                icon = Icons.Rounded.Timer,
                                modifier = chipModifier,
                                visible = true
                            )
                        }

                        // Periods
                        item {
                            MenuItem(
                                label = "Periods",
                                value = "${gameState.periods}",
                                onClick = {
                                    dialogState = DialogState.NumberPicker(
                                        title = "Periods",
                                        initialValue = gameState.periods,
                                        range = 2..4,
                                        onConfirm = { periods ->
                                            onClose()
                                            gameViewModel.setPeriods(periods)
                                            vibrationUtility.vibrateOnce(
                                                50,
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        }
                                    )
                                },
                                icon = Icons.AutoMirrored.Rounded.ViewList,
                                modifier = chipModifier,
                                visible = true
                            )
                        }

                        // Defaults
                        item {
                            MenuItem(
                                label = "Defaults",
                                onClick = {
                                    dialogState = DialogState.Confirmation(
                                        title = "Reset to defaults?",
                                        subText = "All settings will reset to defaults",
                                        onConfirm = { resetGame(30) }
                                    )
                                },
                                icon = Icons.AutoMirrored.Rounded.RotateLeft,
                                modifier = chipModifier,
                                visible = true
                            )
                        }

                        item { Separator() }

                        // Extra Time Settings
                        item {
                            ToggleButton(
                                title = "Enable extra time",
                                secondaryText = "",
                                isChecked = gameState.hasExtraTime,
                                onCheckedChange = { gameViewModel.toggleExtraTime() },
                                visible = true
                            )
                        }

                        item {
                            MenuItem(
                                label = "Extra Time",
                                value = "${gameTimeViewModel.getExtraTimeLength()}",
                                onClick = { },
                                icon = Icons.Rounded.MoreTime,
                                modifier = chipModifier,
                                visible = gameState.hasExtraTime
                            )
                        }

                        item { Separator() }

                        // Display Settings
                        item {
                            ToggleButton(
                                title = "Show Clock",
                                secondaryText = "",
                                isChecked = gameState.showClock,
                                onCheckedChange = { gameViewModel.toggleShowClock() },
                                visible = true
                            )
                        }

                        item {
                            ToggleButton(
                                title = "Extra Info",
                                secondaryText = "Beside clock",
                                isChecked = gameState.showAdditionalInfo,
                                onCheckedChange = { gameViewModel.toggleShowAdditionalInfo() },
                                visible = gameState.showClock
                            )
                        }

                        item {
                            ToggleButton(
                                title = "Keep screen on",
                                secondaryText = "",
                                isChecked = gameState.keepScreenOn,
                                onCheckedChange = { gameViewModel.toggleScreenOn() },
                                visible = true
                            )
                        }

                        item { Separator() }

                        // Close Button
                        item {
                            MenuItem(
                                label = "Close",
                                onClick = onClose,
                                icon = Icons.Rounded.Close,
                                modifier = chipModifier,
                                visible = true,
                                backgroundColor = WearColors.DismissRed,
                                iconTint = WearColors.White
                            )
                        }
                    }
                }
            }
        }

        // Handle Dialogs
        when (val currentDialog = dialogState) {
            is DialogState.Confirmation -> {
                ConfirmationDialog(
                    confirmationQuestion = currentDialog.title,
                    subText = currentDialog.subText,
                    visible = true,
                    onConfirm = {
                        currentDialog.onConfirm()
                        dialogState = DialogState.Hidden
                    },
                    onDismiss = { dialogState = DialogState.Hidden }
                )
            }

            is DialogState.NumberPicker -> {
                MinutePicker(
                    initialValue = currentDialog.initialValue,
                    range = currentDialog.range,
                    vibrationUtility = vibrationUtility,
                    onConfirm = { selectedValue ->
                        currentDialog.onConfirm(selectedValue)
                        dialogState = DialogState.Hidden
                    },
                    onDismiss = { dialogState = DialogState.Hidden },
                    title = currentDialog.title,
                    visible = true
                )
            }

            is DialogState.Report -> {
                MatchReport(
                    visible = true,
                    events = matchReportState.events,
                    closeButtonModifier = chipModifier,
                    onClose = { dialogState = DialogState.Hidden }
                )
            }

            DialogState.Hidden -> { /* No dialog shown */
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun GameActionOverlayPreview() {
    val gameViewModel = GameViewModel(null)
    gameViewModel.setStatus(GameStatus.F_T)
    SettingsMenu(
        onClose = {},
        gameViewModel = gameViewModel,
        gameTimeViewModel = GameTimeViewModel(null, null, null),
        matchReportViewModel = MatchReportViewModel(null),
        vibrationUtility = VibrationUtility(null),
        scalingLazyListState = ScalingLazyListState(),
        visible = true
    )
}