package com.mcgowanb.projects.refereescorekeeper

import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.VibratorManager
import android.view.WindowManager
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
import com.mcgowanb.projects.refereescorekeeper.utility.FormatUtility.Companion.formatGameIntervals
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameTimeViewModel: GameTimeViewModel
    private lateinit var matchReportViewModel: MatchReportViewModel
    private lateinit var vibratorManager: VibratorManager
    private lateinit var vibrationUtility: VibrationUtility
    private lateinit var wakeLock: PowerManager.WakeLock

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var onBackInvokedCallback: OnBackInvokedCallback

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
        gameTimeViewModel = ViewModelProvider(this, factory)[GameTimeViewModel::class.java]
        gameViewModel = ViewModelProvider(this, factory)[GameViewModel::class.java]
        matchReportViewModel = ViewModelProvider(this, factory)[MatchReportViewModel::class.java]

        //this is a callback event that is emitted from the model when the timer runs out
        gameTimeViewModel.setOnPeriodEndCallback {
            lifecycleScope.launch {
                handlePeriodEnd()
            }
        }

        gameViewModel.setScoreEventCallback { team, points, goals, scoreFormat ->
            lifecycleScope.launch {
                addMatchReportEvent(team, points, goals, scoreFormat)
            }
        }

        gameViewModel.setScreenOnCallback { isOn ->
            lifecycleScope.launch {
                updateKeepScreenOn(isOn)
            }
        }

        lifecycleScope.launch {
            gameViewModel.uiState.collect { state ->
                updateKeepScreenOn(state.keepScreenOn)
            }
        }

        setupBackHandling()

        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            MainScreen(
                gameTimerViewModel = gameTimeViewModel,
                gameViewModel = gameViewModel,
                vibrationUtility = vibrationUtility,
                matchReportViewModel = matchReportViewModel
            )
        }
    }

    private fun setupBackHandling() {
        // For Android 33 (Tiramisu) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedCallback = OnBackInvokedCallback {
                handleBackPress()
            }
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                onBackInvokedCallback
            )
        }

        // For older versions and as a fallback
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun handleBackPress() {
        //        Log.d("MainActivity", "On Keydown $keyCode $event");
        lifecycleScope.launch {
            toggleTimer()
        }
    }

    private fun updateKeepScreenOn(enabled: Boolean) {
        if (enabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun addMatchReportEvent(team: Team, points: Int, goals: Int, scoreFormat: String) {
        val time = gameTimeViewModel.formattedElapsedTime.value
        val period = formatGameIntervals(gameViewModel.getModel())
        val message = when {
            goals > 0 -> "P($period) $time\n$team goal, $goals added: \n$scoreFormat"
            goals < 0 -> "P($period) $time\n$team goal reversed, ${-goals} deducted: \n$scoreFormat"
            points > 0 -> "P($period) $time\n$team point, $points added: \n$scoreFormat"
            points < 0 -> "P($period) $time\n$team point reversed, ${-points} deducted: \n$scoreFormat"
            else -> ""
        }
        if (message.isNotEmpty()) {
            matchReportViewModel.addEvent(message)
        }
    }

    private fun toggleTimer(): Boolean {
        lifecycleScope.launch {
            val currentGameStatus = gameViewModel.getGameStatus()
            val isRunning = gameTimeViewModel.isRunning.first()

            when {
                currentGameStatus == GameStatus.NOT_STARTED -> {
                    gameViewModel.startGame()
                    gameTimeViewModel.toggleIsRunning()
                }

                currentGameStatus == GameStatus.PAUSED -> {
                    gameTimeViewModel.toggleIsRunning()
                    gameViewModel.toggleGameStatus()
                }

                currentGameStatus == GameStatus.H_T -> {
                    if (!gameTimeViewModel.isRunning.value) {
                        gameViewModel.toggleGameStatus()
                        gameViewModel.incrementElapsedPeriod()
                    }
                    gameTimeViewModel.toggleIsRunning()
                }

                currentGameStatus == GameStatus.F_T -> {
                    if (gameTimeViewModel.isRunning.value) {
                        gameTimeViewModel.stopTimer()
                    }
                }

                !isRunning && gameTimeViewModel.isOvertime.first() -> {
                    gameViewModel.incrementElapsedPeriod()
                }

                else -> {
                    gameTimeViewModel.toggleIsRunning()
                    gameViewModel.toggleGameStatus()
                }
            }
//
//            Toast.makeText(
//                applicationContext,
//                "${gameViewModel.getGameStatus()} : ${gameViewModel.getElapsedPeriods()}/${gameViewModel.getPeriods()}",
//                Toast.LENGTH_SHORT
//            ).show()
            vibrationUtility.toggleTimer(gameTimeViewModel.isRunning.first())
        }
        return true
    }

    private suspend fun handlePeriodEnd() {
        //this should be done when overtime stops
        val periods = gameViewModel.getPeriods();
        val elapsedPeriods = gameViewModel.getElapsedPeriods()
        if (elapsedPeriods == periods) {
            val ftScore =
                "--------------------\n" +
                        "Full time score: HOME: ${gameViewModel.getHomeScore()}, AWAY: ${gameViewModel.getAwayScore()}" +
                        "\n--------------------"
            matchReportViewModel.addEvent(ftScore)
            gameViewModel.setStatus(GameStatus.F_T)
        } else {
            val halfTimeScore =
                "--------------------\n" +
                        "Half time score: HOME: ${gameViewModel.getHomeScore()}, AWAY: ${gameViewModel.getAwayScore()}" +
                        "\n--------------------"
            matchReportViewModel.addEvent(halfTimeScore)
            gameViewModel.setStatus(GameStatus.H_T)
        }
    }

}