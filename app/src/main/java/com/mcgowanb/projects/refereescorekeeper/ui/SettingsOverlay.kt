package com.mcgowanb.projects.refereescorekeeper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.model.Setting
import com.mcgowanb.projects.refereescorekeeper.ui.button.SettingsButton

@Composable
fun SettingsOverlay(
    showSettings: Boolean,
    onClose: () -> Unit,
    settings: List<Setting>
) {
    AnimatedVisibility(
        visible = showSettings,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = 25.dp),
                state = rememberLazyListState()
            ) {
                items(settings.size) { idx ->
                    SettingsButton(setting = settings.get(idx))
                }
                item {
                    Button(onClick = onClose) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Preview(device = "id:wearos_small_round")
@Composable
private fun SettingsOverlayPreview() {
    SettingsOverlay(true, {}, listOf(
        Setting("periods", "Number of Periods", Icons.Rounded.Functions, 2),
        Setting("periodDurationInMinutes", "Period Duration", Icons.Rounded.HourglassEmpty, 30),
        Setting("periodsPlayed", "Periods Played", Icons.Rounded.MoreTime, 0)
    ))

}