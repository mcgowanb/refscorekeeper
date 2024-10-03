package com.mcgowanb.projects.refereescorekeeper.utility

import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.VibrationEffect.createWaveform
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import com.mcgowanb.projects.refereescorekeeper.enums.VibrationType

@RequiresApi(Build.VERSION_CODES.S)
class VibrationUtility(
    vibratorManager: VibratorManager?
) {
    private val vibrator: Vibrator? = vibratorManager?.defaultVibrator
    val amplitude = DEFAULT_AMPLITUDE
    val doubleBlip = longArrayOf(0, 50, 50, 50)
    val quadBlip = longArrayOf(0, 50, 50, 50, 50, 50, 50, 50, 50)
    val resetBlip = longArrayOf(0, 50, 50, 50, 300, 50, 50, 50, 300, 50, 50, 50, 300, 50, 50, 50)
    val halfTimeBlip = longArrayOf(0, 300, 900, 300, 900, 1200)

    fun getTimerVibration(isRunning: Boolean): VibrationEffect {
        return if (isRunning) {
            getMultiShot(VibrationType.TIME)
        } else {
            getSingleShot()
        }
    }

    fun getMultiShot(type: VibrationType): VibrationEffect {
        val blipType = when (type) {
            VibrationType.SCORE -> doubleBlip
            VibrationType.RESET -> resetBlip
            VibrationType.HALF_TIME -> halfTimeBlip
            else -> quadBlip
        }
        return createWaveform(
            blipType,
            amplitude
        )
    }

    fun getSingleShot(): VibrationEffect {
        return createOneShot(
            50,
            DEFAULT_AMPLITUDE
        )
    }

    fun vibrateOnce(length: Long, amplitude: Int) {
        vibrator?.vibrate(
            createOneShot(
                length,
                amplitude
            )
        )
    }

}