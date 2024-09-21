package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.vector.ImageVector

data class GameAction(
    val description: String,
    val icon: ImageVector,
    val action: () -> Unit
)
