package com.mcgowanb.projects.refereescorekeeper.ui.menu.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.Text

@Composable
fun ButtonLabel(
    text: String?,
    visible: Boolean,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    if (visible) {
        Text(
            text = text ?: "",
            maxLines = maxLines,
            overflow = overflow
        )
    }
}