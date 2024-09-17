package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.Expose
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus

data class MatchSettings(
    @Expose
    val settings: List<Setting> = listOf(
        Setting("periods", "Number of Periods", Icons.Rounded.Functions, 2),
        Setting("periodDurationInMinutes", "Period Duration", Icons.Rounded.HourglassEmpty, 30),
        Setting("periodsPlayed", "Periods Played", Icons.Rounded.MoreTime, 0)
    ),
    val gameActions: List<Setting> = listOf(
        Setting("start", "Start Game", Icons.Rounded.Functions, -1),
        Setting("end", "End Game", Icons.Rounded.SportsScore, -1),
        Setting("summary", "Summary", Icons.AutoMirrored.Rounded.FormatListBulleted, -1),
        Setting("reset", "Reset", Icons.Rounded.PlayCircle, -1)
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
    val gameStatus: GameStatus = GameStatus.NOT_STARTED,
    @Expose
    val extraTimePeriods: Int = 2,
) {
}

data class Setting(
    val key: String,
    val description: String,
    val icon: ImageVector,
    val value: Int
)