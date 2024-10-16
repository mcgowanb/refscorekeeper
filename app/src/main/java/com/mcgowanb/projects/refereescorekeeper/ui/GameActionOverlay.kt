package com.mcgowanb.projects.refereescorekeeper.ui

import android.os.Build
import android.os.VibrationEffect
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreTime
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.button.MenuItem
import com.mcgowanb.projects.refereescorekeeper.ui.button.ToggleButton
import com.mcgowanb.projects.refereescorekeeper.ui.dialog.ConfirmationDialog
import com.mcgowanb.projects.refereescorekeeper.ui.input.MinutePicker
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun GameActionOverlay(
    onClose: () -> Unit,
    gameViewModel: GameViewModel,
    gameTimeViewModel: GameTimeViewModel,
    vibrationUtility: VibrationUtility,
    scalingLazyListState: ScalingLazyListState
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showNumberInput by remember { mutableStateOf(false) }
    var confirmationTitle by remember { mutableStateOf("") }
    var confirmationAction by remember { mutableStateOf({}) }

    var numberPickerTitle by remember { mutableStateOf("") }
    var numberPickerInitialValue by remember { mutableStateOf(0) }
    var numberPickerRange by remember { mutableStateOf(1..30) }
    var numberPickerOnConfirm by remember { mutableStateOf<(Int) -> Unit>({}) }

    val gameState by gameViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val chipModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(vertical = 2.dp)

    val resetGame: () -> Unit = {
        gameViewModel.onAction(ScoreAction.Reset)
        gameTimeViewModel.resetTimer()
        vibrationUtility.vibrateMultiple(VibrationType.CRESCENDO)
        onClose()
    }

    val confirmNewGame: () -> Unit = {
        confirmationTitle = "New Game"
        confirmationAction = resetGame
        showConfirmationDialog = !showConfirmationDialog
    }

    val resetClock: (Int) -> Unit = { selectedMinutes ->
        onClose()
        gameTimeViewModel.setPeriodLength(selectedMinutes)
        scope.launch {
            Toast.makeText(
                context,
                "$selectedMinutes minutes updated successfully",
                Toast.LENGTH_SHORT
            )
                .show()
        }
//        vibrationUtility.vibrateMultiple(VibrationType.RESET, VibrationEffect.DEFAULT_AMPLITUDE)
    }

    val updatePeriods: (Int) -> Unit = { selectedPeriods ->
        onClose()
        gameViewModel.setPeriods(selectedPeriods)
        scope.launch {
            Toast.makeText(
                context,
                "$selectedPeriods halves updated successfully",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        vibrationUtility.vibrateOnce(50, VibrationEffect.DEFAULT_AMPLITUDE)
//        vibrationUtility.vibrateMultiple(VibrationType.RESET, VibrationEffect.DEFAULT_AMPLITUDE)
    }


    Scaffold(
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = scalingLazyListState
            )
        }
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
                item {
                    MenuItem(
                        label = "New Game",
                        onClick = confirmNewGame,
                        icon = {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "Set period time"
                            )
                        },
                        modifier = chipModifier,
                        visible = true
                    )
                }
                item {
                    MenuItem(
                        label = "Minutes",
                        value = "${gameTimeViewModel.getPeriodLength()}",
                        onClick = {
                            numberPickerInitialValue = gameTimeViewModel.getPeriodLength()
                            numberPickerRange = 1..30
                            numberPickerOnConfirm = resetClock
                            showNumberInput = true
                            numberPickerTitle = "Mins"
                        },
                        icon = {
                            Icon(
                                Icons.Rounded.Timer,
                                contentDescription = "Set period time"
                            )
                        },
                        modifier = chipModifier,
                        visible = true

                    )
                }
                item {
                    MenuItem(
                        label = "Periods",
                        value = "${gameState.periods}",
                        onClick = {
                            numberPickerInitialValue = gameState.periods
                            numberPickerRange = 2..4
                            numberPickerOnConfirm = updatePeriods
                            showNumberInput = true
                            numberPickerTitle = "Periods"
                        },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Rounded.ViewList,
                                contentDescription = "Set period time"
                            )
                        },
                        modifier = chipModifier,
                        visible = true
                    )
                }
                item { Separator() }
                item {
                    ToggleButton(
                        title = "Enable extra time",
                        secondaryText = "",
                        isChecked = false,
                        onCheckedChange = { },
                        visible = true
                    )
                }
                item {
                    MenuItem(
                        label = "Extra Time",
                        value = "${gameTimeViewModel.getExtraTimeLength()}",
                        onClick = { },
                        icon = {
                            Icon(
                                Icons.Rounded.MoreTime,
                                contentDescription = "Set period time"
                            )
                        },
                        modifier = chipModifier,
                        visible = true
                    )
                }
                item { Separator() }
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
                        secondaryText = "While game is running",
                        isChecked = false,
                        onCheckedChange = { },
                        visible = true
                    )
                }
                item { Separator() }
                item {
                    Chip(
                        modifier = chipModifier,
                        label = { Text("Close") },
                        onClick = onClose,
                        colors = ChipDefaults.primaryChipColors(
                            backgroundColor = WearColors.DismissRed
                        ),
                        icon = { Icon(Icons.Rounded.Close, contentDescription = "Close") }
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = showConfirmationDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ConfirmationDialog(
            confirmationQuestion = confirmationTitle,
            onConfirm = {
                confirmationAction()
                showConfirmationDialog = false
            },
            onDismiss = { showConfirmationDialog = false }
        )
    }

    AnimatedVisibility(
        visible = showNumberInput,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        MinutePicker(
            initialMinutes = numberPickerInitialValue,
            range = numberPickerRange,
            vibrationUtility = vibrationUtility,
            onConfirm = { selectedValue ->
                numberPickerOnConfirm(selectedValue)
                showNumberInput = false
            },
            onDismiss = { showNumberInput = false },
            title = numberPickerTitle
        )

    }
}

@Composable
fun Separator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(1.dp)
            .background(Color.Gray.copy(alpha = 0.5f))
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun GameActionOverlayPreview() {
    GameActionOverlay(
        onClose = {},
        gameViewModel = GameViewModel(null),
        gameTimeViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null),
        scalingLazyListState = ScalingLazyListState()
    )
}