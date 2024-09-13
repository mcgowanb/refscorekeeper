package com.mcgowanb.projects.refereescorekeeper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun SettingsOverlay(showSettings: Boolean, onClose: () -> Unit) {
    AnimatedVisibility(
        visible = showSettings,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = rememberLazyListState()
            ) {

                items(5) { index ->
                    Button(
                        onClick = { /* TODO: Implement setting change */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Setting ${index}")
                    }
                }
                item {
                    Button(onClick = { /* TODO: Implement setting change */ }) {
                        Text("Setting 2")
                    }
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
@Preview
@Composable
private fun SettingsOverlayPreview() {
    SettingsOverlay(true, { Unit })

}