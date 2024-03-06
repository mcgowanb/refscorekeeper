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
    enableVibrate: Boolean
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    addScore()
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            150
                        )
                    )
                },
                onLongClick = {
                    subtractScore()
                    if (enableVibrate) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                200,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    }
                }
            )
            .then(modifier)
    )
}