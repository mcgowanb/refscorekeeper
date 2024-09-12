package com.mcgowanb.projects.refereescorekeeper.screen

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreActionBox(
    modifier: Modifier,
    subtractScore: () -> Unit,
    addScore: () -> Unit,
    shouldVibrate: Boolean
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
                        VibrationEffect.createPredefined(
                            VibrationEffect.EFFECT_CLICK
                        )
                    )
                },
                onDoubleClick = {
                    subtractScore()
                    if (shouldVibrate) {
                        vibrator.vibrate(
                            VibrationEffect.createPredefined(
                                VibrationEffect.EFFECT_DOUBLE_CLICK
                            )
                        )
                    }
                }
            )
            .then(modifier)
    )
}