package com.mcgowanb.projects.refereescorekeeper.ui

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.Star
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
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.model.Setting
import com.mcgowanb.projects.refereescorekeeper.ui.button.GameActionButton
import com.mcgowanb.projects.refereescorekeeper.ui.button.SettingsButton
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
        vibrationUtility.vibrateMultiple(
            VibrationType.RESET,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
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
        vibrationUtility.vibrateMultiple(
            VibrationType.RESET,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
    }

    val endGame: () -> Unit = {
        //todo - reset game
//        gameViewModel.updateGameState(GameStatus.COMPLETED)
        onClose()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameActionButton(
                title = "New Game",
                icon = Icons.Rounded.Star,
                action = confirmNewGame,
                iconColor = WearColors.White,
                backgroundColor = WearColors.DarkGray
            )
            SettingsButton(
                setting = Setting(
                    "mins",
                    "Period time",
                    Icons.Rounded.HourglassEmpty,
                    gameTimerViewModel.getPeriodLength()
                ),
                onClick = { showNumberInput = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameActionButton(
                title = "Close",
                icon = Icons.Rounded.HighlightOff,
                action = onClose,
                iconColor = WearColors.White,
                backgroundColor = WearColors.DismissRed
            )
            Spacer(modifier = Modifier.height(26.dp))
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
            onDismiss = {
                showNumberInput = false
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun GameActionOverlayPreview() {
    GameActionOverlay(
        onClose = {},
        gameViewModel = GameViewModel(null),
        gameTimerViewModel = GameTimeViewModel(null, null, null),
        vibrationUtility = VibrationUtility(null)
    )
}