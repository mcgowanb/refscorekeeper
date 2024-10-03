package com.mcgowanb.projects.refereescorekeeper.ui.screen

import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreActionBox(
    modifier: Modifier,
    subtractScore: () -> Unit,
    addScore: () -> Unit,
    shouldVibrate: Boolean,
    vibrationUtility: VibrationUtility
) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    addScore()
                    vibrationUtility.vibrateOnce(50, VibrationEffect.DEFAULT_AMPLITUDE)
                },
                onDoubleClick = {
                    subtractScore()
                    if (shouldVibrate) {
                        vibrationUtility.vibrateMultiple(
                            VibrationType.SCORE,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    }
                }
            )
            .then(modifier)
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun ScoreActionBoxPreview() {
    ScoreActionBox(
        modifier = Modifier,
        {},
        {},
        true,
        vibrationUtility = VibrationUtility(null)
    )
}