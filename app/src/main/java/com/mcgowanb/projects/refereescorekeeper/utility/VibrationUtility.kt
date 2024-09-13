package com.mcgowanb.projects.refereescorekeeper.utility

import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.VibrationEffect.createWaveform
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType

class VibrationUtility() {

    val amplitude = DEFAULT_AMPLITUDE
    val doubleBlip = longArrayOf(0, 50, 50, 50)
    val quadBlip = longArrayOf(0, 50, 50, 50, 50, 50, 50, 50, 50)

    fun getTimerVibration(isRunning: Boolean): VibrationEffect {
        return if (isRunning) {
            getMultiShot(VibrationType.TIME)
        } else {
            getSingleShot()
        }
    }

    fun getMultiShot(type: VibrationType): VibrationEffect {
        return createWaveform(
            if (type == VibrationType.SCORE) doubleBlip else quadBlip,
            amplitude
        )
    }

    fun getSingleShot(): VibrationEffect {
        return createOneShot(
            50,
            DEFAULT_AMPLITUDE
        )
    }
}