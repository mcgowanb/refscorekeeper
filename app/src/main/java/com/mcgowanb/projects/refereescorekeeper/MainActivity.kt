/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.mcgowanb.projects.refereescorekeeper

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.google.gson.GsonBuilder
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.screen.Watchface
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import com.mcgowanb.projects.refereescorekeeper.utility.KeepScreenOn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameTimerViewModel: GameTimeViewModel
    private lateinit var vibratorManager: VibratorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.init(this.application, gson)

        gameTimerViewModel = ViewModelProvider(this).get(GameTimeViewModel::class.java)
        gameTimerViewModel.init()

        vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                // Prevent the app from fully stopping
                moveTaskToBack(false)
            }
        })

        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            KeepScreenOn()
            Scaffold(
                timeText = {
                    TimeText(
                        timeTextStyle = TimeTextDefaults
                            .timeTextStyle(fontSize = 14.sp)
                    )
                },
            ) {
                RefereeScoreKeeperTheme {
                    Watchface(gameViewModel, gameTimerViewModel)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return handlePhysicalButtonEvent(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    private fun handlePhysicalButtonEvent(keyCode: Int, event: KeyEvent?): Boolean {

        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> toggleTimer()
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun toggleTimer(): Boolean {
        lifecycleScope.launch {
            vibratorManager.defaultVibrator.vibrate(
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            )
            gameTimerViewModel.toggleIsRunning()
        }
        return true
    }

}