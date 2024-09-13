package com.mcgowanb.projects.refereescorekeeper.ui.screen

import android.os.Vibrator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreActionBox(
    modifier: Modifier,
    subtractScore: () -> Unit,
    addScore: () -> Unit,
    shouldVibrate: Boolean,
    vibrationUtility: VibrationUtility
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    addScore()
                    vibrator.vibrate(
                        vibrationUtility.getSingleShot()
                    )
                },
                onDoubleClick = {
                    subtractScore()
                    if (shouldVibrate) {
                        vibrator.vibrate(
                            vibrationUtility.getMultiShot(VibrationType.SCORE)
                        )
                    }
                }
            )
            .then(modifier)
    )
}