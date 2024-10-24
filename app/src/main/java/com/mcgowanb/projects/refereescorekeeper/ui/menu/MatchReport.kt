package com.mcgowanb.projects.refereescorekeeper.ui.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.ui.animation.SlideUpVertically
import com.mcgowanb.projects.refereescorekeeper.ui.menu.button.MenuItem
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MatchReport(
    visible: Boolean,
    events: List<String>,
    onClose: () -> Unit,
    closeButtonModifier: Modifier
) {
    val scalingLazyListState = rememberScalingLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(visible) {
        if (visible) {
            scope.launch {
                scalingLazyListState.animateScrollToItem(0)
            }
        }
    }

    SlideUpVertically(
        visible = visible
    ) {
        Scaffold(
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = scalingLazyListState
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WearColors.Black)
            ) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = scalingLazyListState,
                    contentPadding = PaddingValues(
                        top = 40.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 40.dp
                    )
                ) {
                    items(events.size) { event ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = 4.dp)
                                .background(
                                    color = WearColors.Purple.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = events[event],
                                color = WearColors.White,
                                style = androidx.wear.compose.material.MaterialTheme.typography.body1,
                            )
                        }
                    }
                    item {
                        MenuItem(
                            label = "Close",
                            onClick = onClose,
                            icon = Icons.Rounded.Close,
                            modifier = closeButtonModifier,
                            visible = true,
                            backgroundColor = WearColors.DismissRed,
                            iconTint = WearColors.White
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun MatchReportPreview() {

    MatchReport(
        onClose = {},
        visible = true,
        closeButtonModifier = Modifier,
        events = listOf(
            "HOME goal, 1 added: \n01:00 : 00:00",
            "AWAY point, 1 added: \n01:00 : 00:01",
            "AWAY point, 1 added: \n01:00 : 00:02",
            "AWAY point, 1 added: \n01:00 : 00:03"
        )
    )
}