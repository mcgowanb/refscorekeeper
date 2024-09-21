package com.mcgowanb.projects.refereescorekeeper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.RotateLeft
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.model.GameAction
import com.mcgowanb.projects.refereescorekeeper.ui.button.GameActionButton

@Composable
fun GameActionOverlay(
    isVisible: Boolean,
    onClose: () -> Unit,
    settings: List<GameAction>
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            LazyColumn(
                modifier = Modifier.padding(top = 35.dp),
                state = rememberLazyListState()
            ) {
                items(settings.size) { idx ->
                    GameActionButton(setting = settings.get(idx))
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    GameActionButton(
                        setting = GameAction("Close", Icons.Rounded.HighlightOff, onClose)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(26.dp))
                }
            }
        }
    }
}


@Preview(device = "id:wearos_small_round")
@Composable
private fun GameActionOverlayPreview() {
    GameActionOverlay(
        true, {}, listOf(
            GameAction("Reset Game", Icons.AutoMirrored.Rounded.RotateLeft) { },
            GameAction("Reset Game", Icons.AutoMirrored.Rounded.RotateLeft) { }
        )
    )

}