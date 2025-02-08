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
    val tripleBlip = longArrayOf(0, 50, 50, 50, 50, 50, 50)
    val quadBlip = longArrayOf(0, 50, 50, 50, 50, 50, 50, 50, 50)
    val resetBlip = longArrayOf(0, 50, 50, 50, 300, 50, 50, 50, 300, 50, 50, 50, 300, 50, 50, 50)
    val halfTimeBlip = longArrayOf(0, 300, 900, 300, 900, 1200)
    val crescendoBlip = longArrayOf(0, 100, 100, 200, 100, 300, 100, 400)
    val sosBlip = longArrayOf(0, 100, 100, 100, 100, 100, 100, 300, 300, 300, 100, 100, 100)
    val heartbeatBlip = longArrayOf(0, 100, 100, 100, 400)

    private fun getMultiShot(type: VibrationType): VibrationEffect {
        val blipType = when (type) {
            VibrationType.ADD_SCORE -> doubleBlip
            VibrationType.RESET -> resetBlip
            VibrationType.HALF_TIME -> halfTimeBlip
            else -> quadBlip
        }
        return createWaveform(
            blipType,
            amplitude
        )
    }

    private fun getSingleShot(): VibrationEffect {
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

    fun vibrateMultiple(type: VibrationType, amplitude: Int = DEFAULT_AMPLITUDE) {
        when (type) {
            VibrationType.ADD_SCORE -> vibrator?.vibrate(createOneShot(50, amplitude))
            VibrationType.SUBTRACT_SCORE -> vibrator?.vibrate(createWaveform(doubleBlip, amplitude))
            VibrationType.RESET -> vibrator?.vibrate(createWaveform(resetBlip, amplitude))
            VibrationType.HALF_TIME -> vibrator?.vibrate(createWaveform(tripleBlip, amplitude))
            VibrationType.TIME -> vibrator?.vibrate(createWaveform(quadBlip, amplitude))
            VibrationType.CRESCENDO -> vibrateCrescendo()
            VibrationType.SOS -> vibrateSOS()
            VibrationType.HEARTBEAT -> vibrateHeartbeat(3)
            VibrationType.CUSTOM -> vibrateCustomIntensity()
            VibrationType.CONTINUOUS -> vibrateContinuous(2000)
        }
    }

    fun vibrateCustomIntensity() {
        val timings = longArrayOf(0, 100, 200, 100, 300, 100)
        val amplitudes = intArrayOf(0, 50, 0, 100, 0, 150)
        vibrator?.vibrate(createWaveform(timings, amplitudes, -1))
    }

    fun vibrateContinuous(durationMs: Long) {
        vibrator?.vibrate(createOneShot(durationMs, amplitude))
    }

    fun vibrateCrescendo() {
        vibrator?.vibrate(createWaveform(crescendoBlip, amplitude))
    }

    fun vibrateSOS() {
        vibrator?.vibrate(createWaveform(sosBlip, amplitude))
    }

    fun vibrateHeartbeat(repetitions: Int) {
        val repeatedPattern = repeatPattern(heartbeatBlip, repetitions)
        vibrator?.vibrate(createWaveform(repeatedPattern, amplitude))
    }

    fun repeatPattern(pattern: LongArray, repetitions: Int): LongArray {
        return LongArray(pattern.size * repetitions) { i ->
            pattern[i % pattern.size]
        }
    }

    fun toggleTimer(isStarting: Boolean) {
        vibrator?.vibrate(
            if (isStarting) {
                getSingleShot()
            } else {
                getMultiShot(VibrationType.TIME)

            }
        )
    }

    fun click() {
        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }

    fun doubleClick() {
        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
    }

    fun heavyClick() {
        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
    }


}