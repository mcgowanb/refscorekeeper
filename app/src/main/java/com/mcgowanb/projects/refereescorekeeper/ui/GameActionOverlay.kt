package com.mcgowanb.projects.refereescorekeeper.ui

import android.os.Build
import android.os.Vibrator
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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.GameAction
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
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    var confirmationTitle by remember { mutableStateOf("") }
    var confirmationAction by remember { mutableStateOf({}) }

    val resetGame: () -> Unit = {
        gameViewModel.onAction(ScoreAction.Reset)
        gameTimerViewModel.resetTimer()
        onClose()
        vibrator.vibrate(vibrationUtility.getMultiShot(VibrationType.RESET))
    }

    val endGame: () -> Unit = {
        gameViewModel.updateGameState(GameStatus.COMPLETED)
        onClose()
        vibrator.vibrate(vibrationUtility.getMultiShot(VibrationType.RESET))
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
                .verticalScroll(rememberScrollState())
        ) {
            GameActionButton(
                setting = GameAction("New Game", Icons.Rounded.Add) {
                    confirmationTitle = "Start New Game?"
                    confirmationAction = resetGame
                    showConfirmationDialog = true
                }
            )
            SettingsButton(
                setting = Setting(
                    "mins",
                    "Period time",
                    Icons.Rounded.AccessTime,
                    gameTimerViewModel.getPeriodLength()
                ),
                onClick = { showNumberInput = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameActionButton(
                setting = GameAction("Close", Icons.Rounded.HighlightOff, onClose)
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
            minutes = gameTimerViewModel.getPeriodLength(),
            onConfirm = { selectedMinutes ->
                showNumberInput = false
                //confirmation dialog
                //show toast
                gameTimerViewModel.setPeriodLength(selectedMinutes)
                onClose()
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
        {},
        GameViewModel(),
        GameTimeViewModel(),
        VibrationUtility()
    )

}