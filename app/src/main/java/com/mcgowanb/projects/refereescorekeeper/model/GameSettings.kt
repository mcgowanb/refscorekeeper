package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.Expose
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus

data class GameSettings(
    @Expose
    val settings: List<Setting> = listOf(
        Setting("periods", "Number of Periods", Icons.Rounded.Functions, 2),
        Setting("periodDurationInMinutes", "Period Duration", Icons.Rounded.HourglassEmpty, 30),
        Setting("periodsPlayed", "Periods Played", Icons.Rounded.MoreTime, 0)
    ),
    @Expose
    val periodsPlayed: Int = 0,
    @Expose
    val hTeamHtGoals: Int = 0,
    @Expose
    val hTeamHtPoints: Int = 0,
    @Expose
    val aTeamHtGoals: Int = 0,
    @Expose
    val aTeamHtPoints: Int = 0,
    @Expose
    val gameStatus: GameStatus = GameStatus.NOT_STARTED
) {
}

data class Setting(
    val key: String,
    val description: String,
    val icon: ImageVector,
    val value: Any
)