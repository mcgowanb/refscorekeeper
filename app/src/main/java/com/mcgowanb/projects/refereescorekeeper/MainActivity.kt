package com.mcgowanb.projects.refereescorekeeper

import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.VibratorManager
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.MainScreen
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameTimerViewModel: GameTimeViewModel
    private lateinit var vibratorManager: VibratorManager
    private lateinit var vibrationUtility: VibrationUtility
    private lateinit var wakeLock: PowerManager.WakeLock


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        vibrationUtility = VibrationUtility()
        vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.init(this.application, gson)

        gameTimerViewModel = ViewModelProvider(this).get(GameTimeViewModel::class.java)
        gameTimerViewModel.init(this.application, gson, vibrationUtility, vibratorManager)

        val powerManager = getSystemService<PowerManager>()!!
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            MainScreen(
                gameTimerViewModel,
                gameViewModel,
                vibrationUtility
            )
        }
    }

    override fun onResume() {
        super.onResume()
        wakeLock.acquire(35 * 60 * 1000L)
    }

    override fun onPause() {
        super.onPause()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) {
            wakeLock.release()
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
                vibrationUtility.getTimerVibration(gameTimerViewModel.isRunning.value)
            )
            gameTimerViewModel.toggleIsRunning()
        }
        return true
    }

}