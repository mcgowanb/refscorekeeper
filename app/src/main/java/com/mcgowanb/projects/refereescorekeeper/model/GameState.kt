package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.Color

data class GameState(
    val hPoints: Int = 0,
    val hGoals: Int = 0,
    val aPoints: Int = 0,
    val aGoals: Int = 0
) {
    private val totalHomeScoreInt: Int = (hGoals * 3) + hPoints
    private val totalAwayScoreInt: Int = (aGoals * 3) + aPoints

    val homeScore: String = "${hGoals.twoDigitFormat()}:${hPoints.twoDigitFormat()}"
    val awayScore: String = "${aGoals.twoDigitFormat()}:${aPoints.twoDigitFormat()}"

    val totalHomeScore: String = "(${totalHomeScoreInt})"
    val totalAwayScore: String = "(${totalAwayScoreInt})"


    val homeColor: Color
        get() = when {
            totalHomeScoreInt == totalAwayScoreInt -> Color.Black
            totalHomeScoreInt > totalAwayScoreInt -> Color.Green
            else -> Color.Red
        }

    val awayColor: Color
        get() = when {
            totalHomeScoreInt == totalAwayScoreInt -> Color.Black
            totalHomeScoreInt < totalAwayScoreInt -> Color.Green
            else -> Color.Red
        }

    private fun Int.twoDigitFormat(): String = "%02d".format(this)

}
