package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.Color

data class GameState(
    var hPoints: Int = 0,
    var hGoals: Int = 0,
    var aPoints: Int = 0,
    var aGoals: Int = 0
) {
    val homeScore: String = String.format("%02d", hGoals) + ":" + String.format("%02d", hPoints)
    val awayScore: String = String.format("%02d", aGoals) + ":" + String.format("%02d", aPoints)

    private val totalHomeScoreInt: Int = (hGoals * 3) + hPoints
    val totalAwayScoreInt: Int = (aGoals * 3) + aPoints

    val totalHomeScore: String = String.format("(%s)", totalHomeScoreInt)
    val totalAwayScore: String = String.format("(%s)", totalAwayScoreInt)

    val homeColor: Color
        get() {
            if (totalHomeScore == totalAwayScore) {
                return Color.Black
            }
            if (totalHomeScore > totalAwayScore) {
                return Color.Green
            }
            return Color.Red
        }

    val awayColor: Color
        get() {
            if (totalHomeScore == totalAwayScore) {
                return Color.Black
            }
            if (totalHomeScore < totalAwayScore) {
                return Color.Green
            }
            return Color.Red
        }
}
