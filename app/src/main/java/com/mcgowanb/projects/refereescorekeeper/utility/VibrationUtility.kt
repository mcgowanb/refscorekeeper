package com.mcgowanb.projects.refereescorekeeper.utility

import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.VibrationEffect.createWaveform

class VibrationUtility() {

    val amplitude = DEFAULT_AMPLITUDE
    val vibrationPattern = longArrayOf(0, 50, 50, 50)

    fun getVibrationEffect(isRunning: Boolean): VibrationEffect {
        return if (isRunning) {
            doubleClick()
        } else {
            singleShot()
        }
    }

    fun doubleClick(): VibrationEffect {
        return createWaveform(
            vibrationPattern,
            amplitude
        )
    }

    fun singleShot(): VibrationEffect {
        return createOneShot(
            50,
            DEFAULT_AMPLITUDE
        )
    }
}