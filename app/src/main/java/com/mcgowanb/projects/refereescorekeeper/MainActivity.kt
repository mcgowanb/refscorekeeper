package com.mcgowanb.projects.refereescorekeeper

import android.os.Build
import android.os.Bundle
import android.os.VibratorManager
import android.view.WindowManager
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
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
    private val gson by lazy {
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    private val vibrationUtility by lazy {
        VibrationUtility(getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager)
    }

    private val viewModels: Triple<GameTimeViewModel, GameViewModel, MatchReportViewModel> by lazy {
        val soundUtility = SoundUtility(applicationContext)
        val fileHandlerUtility = FileHandlerUtility(applicationContext, gson)
        val factory = ViewModelFactory(fileHandlerUtility, vibrationUtility, soundUtility)

        Triple(
            ViewModelProvider(this, factory)[GameTimeViewModel::class.java],
            ViewModelProvider(this, factory)[GameViewModel::class.java],
            ViewModelProvider(this, factory)[MatchReportViewModel::class.java]
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModelCallbacks()
        setupBackHandling()
        setupScreenHandling()
        setupUI()
    }

    private fun setupViewModelCallbacks() {
        val (gameTimeViewModel, gameViewModel, matchReportViewModel) = viewModels

        gameTimeViewModel.setOnPeriodEndCallback {
            lifecycleScope.launch { handlePeriodEnd() }
        }

        gameViewModel.apply {
            setScoreEventCallback { team, points, goals, scoreFormat ->
                lifecycleScope.launch {
                    createMatchReportEvent(team, points, goals, scoreFormat)?.let {
                        matchReportViewModel.addEvent(it)
                    }
                }
            }

            setScreenOnCallback { isOn ->
                lifecycleScope.launch { updateKeepScreenOn(isOn) }
            }
        }

        // Observe UI state for screen handling
        lifecycleScope.launch {
            gameViewModel.uiState.collect { state ->
                updateKeepScreenOn(state.keepScreenOn)
            }
        }
    }

    private fun setupBackHandling() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) { handleBackPress() }
        }

        onBackPressedDispatcher.addCallback(this) { handleBackPress() }
    }

    private fun setupScreenHandling() {
        lifecycleScope.launch {
            viewModels.second.uiState.collect { state ->
                updateKeepScreenOn(state.keepScreenOn)
            }
        }
    }

    private fun setupUI() {
        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        val (gameTimeViewModel, gameViewModel, matchReportViewModel) = viewModels

        setContent {
            MainScreen(
                gameTimerViewModel = gameTimeViewModel,
                gameViewModel = gameViewModel,
                vibrationUtility = vibrationUtility,
                matchReportViewModel = matchReportViewModel
            )
        }
    }

    private fun handleBackPress() {
        lifecycleScope.launch { toggleTimer() }
    }

    private fun updateKeepScreenOn(enabled: Boolean) {
        val flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        if (enabled) window.addFlags(flags) else window.clearFlags(flags)
    }

    private fun createMatchReportEvent(
        team: Team,
        points: Int,
        goals: Int,
        scoreFormat: String
    ): String? {
        val (gameTimeViewModel, gameViewModel) = viewModels
        val time = gameTimeViewModel.formattedElapsedTime.value
        val period = formatGameIntervals(gameViewModel.getModel())

        return when {
            goals > 0 -> "P($period) $time\n$team goal, $goals added: \n$scoreFormat"
            goals < 0 -> "P($period) $time\n$team goal reversed, ${-goals} deducted: \n$scoreFormat"
            points > 0 -> "P($period) $time\n$team point, $points added: \n$scoreFormat"
            points < 0 -> "P($period) $time\n$team point reversed, ${-points} deducted: \n$scoreFormat"
            else -> null
        }
    }

    private suspend fun toggleTimer(): Boolean {
        val (gameTimeViewModel, gameViewModel) = viewModels
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

            currentGameStatus == GameStatus.F_T && gameTimeViewModel.isRunning.value -> {
                gameTimeViewModel.stopTimer()
            }

            !isRunning && gameTimeViewModel.isOvertime.first() -> {
                gameViewModel.incrementElapsedPeriod()
            }

            else -> {
                gameTimeViewModel.toggleIsRunning()
                gameViewModel.toggleGameStatus()
            }
        }

        vibrationUtility.toggleTimer(gameTimeViewModel.isRunning.first())
        return true
    }

    private suspend fun handlePeriodEnd() {
        val (_, gameViewModel, matchReportViewModel) = viewModels
        val periods = gameViewModel.getPeriods()
        val elapsedPeriods = gameViewModel.getElapsedPeriods()

        val scoreMessage = buildString {
            append("--------------------\n")
            append(if (elapsedPeriods == periods) "Full" else "Half")
            append(" time score: HOME: ${gameViewModel.getHomeScore()}, ")
            append("AWAY: ${gameViewModel.getAwayScore()}\n")
            append("--------------------")
        }

        matchReportViewModel.addEvent(scoreMessage)
        gameViewModel.setStatus(
            if (elapsedPeriods == periods) GameStatus.F_T else GameStatus.H_T
        )
    }
}