package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.Expose
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus

data class GameState(
    @Expose val hPoints: Int = 0,
    @Expose val hGoals: Int = 0,
    @Expose val aPoints: Int = 0,
    @Expose val aGoals: Int = 0,
    @Expose val periods: Int = 2,
    @Expose val elapsedPeriods: Int = 0,
    @Expose val status: GameStatus = GameStatus.NOT_STARTED,
    @Expose val showAdditionalInfo: Boolean = true,
    @Expose val showClock: Boolean = true,
    @Expose val hasExtraTime: Boolean = false
) {
    val totalHomeScoreInt: Int get() = (hGoals * 3) + hPoints
    val totalAwayScoreInt: Int get() = (aGoals * 3) + aPoints

    val homeScore: String get() = "${hGoals.twoDigitFormat()}:${hPoints.twoDigitFormat()}"
    val awayScore: String get() = "${aGoals.twoDigitFormat()}:${aPoints.twoDigitFormat()}"

    val totalHomeScore: String get() = totalHomeScoreInt.toString()
    val totalAwayScore: String get() = totalAwayScoreInt.toString()

    val homeDiff: String get() = "(${totalHomeScoreInt - totalAwayScoreInt})"
    val awayDiff: String get() = "(${totalAwayScoreInt - totalHomeScoreInt})"

    val homeColor: Color
        get() = when {
            totalHomeScoreInt == totalAwayScoreInt -> Color.Black
            totalHomeScoreInt > totalAwayScoreInt -> WearColors.ConfirmGreen
            else -> WearColors.DismissRed
        }

    val awayColor: Color
        get() = when {
            totalHomeScoreInt == totalAwayScoreInt -> Color.Black
            totalHomeScoreInt < totalAwayScoreInt -> WearColors.ConfirmGreen
            else -> WearColors.DismissRed
        }

    private fun Int.twoDigitFormat(): String = "%02d".format(this)
}