package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreActionBox(
    modifier: Modifier,
    onDoubleClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                    onLongClick()
                },
                onDoubleClick = {
                    onDoubleClick()
                }
            )
            .then(modifier)
    )
}