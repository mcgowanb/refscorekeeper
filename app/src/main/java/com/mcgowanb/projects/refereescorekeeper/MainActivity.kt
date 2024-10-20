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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus
import com.mcgowanb.projects.refereescorekeeper.enums.Team
import com.mcgowanb.projects.refereescorekeeper.factory.ViewModelFactory
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.model.MatchReportViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.main.MainScreen
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameTimerViewModel: GameTimeViewModel
    private lateinit var matchReportViewModel: MatchReportViewModel
    private lateinit var vibratorManager: VibratorManager
    private lateinit var vibrationUtility: VibrationUtility
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibrationUtility = VibrationUtility(vibratorManager)
        val soundUtility = SoundUtility(this.applicationContext)
        val fileHandlerUtility = FileHandlerUtility(this.applicationContext, gson)

        val factory = ViewModelFactory(fileHandlerUtility, vibrationUtility, soundUtility)
        gameTimerViewModel = ViewModelProvider(this, factory)[GameTimeViewModel::class.java]
        gameViewModel = ViewModelProvider(this, factory)[GameViewModel::class.java]
        matchReportViewModel = ViewModelProvider(this, factory)[MatchReportViewModel::class.java]

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        gameTimerViewModel.setOnPeriodEndCallback {
            lifecycleScope.launch {
                handlePeriodEnd()
            }
        }

        gameViewModel.setScoreEventCallback { team, points, goals ->
            lifecycleScope.launch {
                addMatchReportEvent(team, points, goals)
            }
        }

        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            MainScreen(
                gameTimerViewModel = gameTimerViewModel,
                gameViewModel = gameViewModel,
                vibrationUtility = vibrationUtility,
                matchReportViewModel = matchReportViewModel
            )
        }
    }

    private fun addMatchReportEvent(team: Team, points: Int, goals: Int) {

        val message = when {
            goals > 0 -> "$team goal, $goals added"
            goals < 0 -> "$team goal reversed, ${-goals} deducted"
            points > 0 -> "$team point, $points added"
            points < 0 -> "$team point reversed, ${-points} deducted"
            else -> "No change in score for $team team"
        }
        matchReportViewModel.addEvent(message)
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
            val currentGameStatus = gameViewModel.getGameStatus()
            val isRunning = gameTimerViewModel.isRunning.first()

            when {
                currentGameStatus == GameStatus.NOT_STARTED -> {
                    gameViewModel.startGame()
                    gameTimerViewModel.toggleIsRunning()
                }

                currentGameStatus == GameStatus.PAUSED -> {
                    gameTimerViewModel.toggleIsRunning()
                    gameViewModel.toggleGameStatus()
                }

                currentGameStatus == GameStatus.H_T -> {
                    if (!gameTimerViewModel.isRunning.value) {
                        gameViewModel.toggleGameStatus()
                        gameViewModel.incrementElapsedPeriod()
                    }
                    gameTimerViewModel.toggleIsRunning()
                }

                currentGameStatus == GameStatus.F_T -> {
                    if (gameTimerViewModel.isRunning.value) {
                        gameTimerViewModel.stopTimer()
                    }
                }

                !isRunning && gameTimerViewModel.isOvertime.first() -> {
                    gameViewModel.incrementElapsedPeriod()
                }

                else -> {
                    gameTimerViewModel.toggleIsRunning()
                    gameViewModel.toggleGameStatus()
                }
            }
            vibrationUtility.toggleTimer(gameTimerViewModel.isRunning.first())
        }
        return true
    }

    private suspend fun handlePeriodEnd() {
        val periods = gameViewModel.getPeriods();
        val elapsedPeriods = gameViewModel.getElapsedPeriods()
        if (elapsedPeriods == periods) {
            gameViewModel.setStatus(GameStatus.F_T)
        } else {
            gameViewModel.setStatus(GameStatus.H_T)
        }
    }

}