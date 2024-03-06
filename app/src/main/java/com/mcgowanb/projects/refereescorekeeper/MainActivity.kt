/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.mcgowanb.projects.refereescorekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.mcgowanb.projects.refereescorekeeper.model.RefScoreViewModel
import com.mcgowanb.projects.refereescorekeeper.screen.Watchface
import com.mcgowanb.projects.refereescorekeeper.theme.RefereeScoreKeeperTheme
import com.mcgowanb.projects.refereescorekeeper.utility.KeepScreenOn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            KeepScreenOn()
            Scaffold(
                timeText = {
                    TimeText(
                        timeTextStyle = TimeTextDefaults
                            .timeTextStyle(fontSize = 8.sp)
                    )
                },
            ) {
                RefereeScoreKeeperTheme {
                    val viewModel = viewModel<RefScoreViewModel>()
                    val state = viewModel.state
                    Watchface(
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}