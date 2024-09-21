package com.mcgowanb.projects.refereescorekeeper.ui

import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.RotateLeft
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
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.model.GameAction
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.button.GameActionButton
import com.mcgowanb.projects.refereescorekeeper.ui.dialog.ConfirmationDialog
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@Composable
fun GameActionOverlay(
    isVisible: Boolean,
    onClose: () -> Unit,
    gameViewModel: GameViewModel,
    gameTimerViewModel: GameTimeViewModel,
    vibrationUtility: VibrationUtility
) {

    var showConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)


    val resetGame: () -> Unit = {
        gameViewModel.onAction(ScoreAction.Reset)
        gameTimerViewModel.resetTimer()
        onClose()
        showConfirmationDialog = false
        vibrator.vibrate(
            vibrationUtility.getMultiShot(VibrationType.RESET)
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (isVisible) {
                Column(
                    modifier = Modifier.padding(top = 35.dp)
                ) {
                    GameActionButton(setting =
                    GameAction("Reset Game", Icons.AutoMirrored.Rounded.RotateLeft) {
                        showConfirmationDialog = true
                    }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GameActionButton(
                        setting = GameAction("Close", Icons.Rounded.HighlightOff, onClose)
                    )
                    Spacer(modifier = Modifier.height(26.dp))
                }
            }
        }
        if (showConfirmationDialog) {
            ConfirmationDialog(
                confirmationQuestion = "Reset Game?",
                onConfirm = resetGame,
                onDismiss = { showConfirmationDialog = false }
            )
        }
    }
}


@Preview(device = "id:wearos_small_round")
@Composable
private fun GameActionOverlayPreview() {
    GameActionOverlay(
        true, {},
        GameViewModel(),
        GameTimeViewModel(),
        VibrationUtility()
    )

}