package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mcgowanb.projects.refereescorekeeper.ui.animation.MenuItemAnimation

@Composable
fun Separator(visible: Boolean = true) {
    MenuItemAnimation(
        visible = visible
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(1.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}