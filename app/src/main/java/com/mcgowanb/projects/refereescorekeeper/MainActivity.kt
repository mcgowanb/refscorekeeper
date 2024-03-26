/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.mcgowanb.projects.refereescorekeeper

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.google.gson.GsonBuilder
import com.mcgowanb.projects.refereescorekeeper.action.ScoreAction
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.screen.Watchface
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var gameViewModel: GameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.init(this.application, gson)

        installSplashScreen()
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            Scaffold(
                timeText = {
                    TimeText(
                        timeTextStyle = TimeTextDefaults
                            .timeTextStyle(fontSize = 14.sp)
                    )
                },
            ) {
                RefereeScoreKeeperTheme {
                    Watchface(gameViewModel)
                }
            }
        }
    }

    private var lastPressTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastPressTime < 1000) {
                // Double press detected
                handleButtonPress(ButtonType.DOUBLE)
                // Reset last press time
                lastPressTime = 0
            } else {
                // Single press detected
                handleButtonPress(ButtonType.SINGLE)
                // Update last press time
                lastPressTime = currentTime
            }
        }
        // Handle other button presses if needed
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_STEM_1) {
//            // Handle physical button release
//            handleButtonRelease(ButtonType.SINGLE)
//            return true
//        }
        return false
        // Handle other button releases if needed
//        return super.onKeyUp(keyCode, event)
    }

    private fun handleButtonPress(buttonType: ButtonType) {
        lifecycleScope.launch {
            gameViewModel.onAction(ScoreAction.Reset)
        }
    }

    private fun handleButtonRelease(buttonType: ButtonType) {
        // Handle button release actions here
    }
}

enum class ButtonType {
    SINGLE,
    DOUBLE,
    LONG
}
