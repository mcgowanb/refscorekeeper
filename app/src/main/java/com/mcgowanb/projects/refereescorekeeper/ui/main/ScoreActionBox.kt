package com.mcgowanb.projects.refereescorekeeper.ui.main

import android.os.Build
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
                    vibrationUtility.vibrateMultiple(VibrationType.ADD_SCORE)
                },
                onDoubleClick = {
                    subtractScore()
                    if (shouldVibrate) {
                        vibrationUtility.vibrateMultiple(VibrationType.SUBTRACT_SCORE)
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