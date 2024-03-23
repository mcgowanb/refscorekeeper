package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.Expose

data class GameState(
    @Expose
    val hPoints: Int = 0,
    @Expose
    val hGoals: Int = 0,
    @Expose
    val aPoints: Int = 0,
    @Expose
    val aGoals: Int = 0
) {
    private val _totalHomeScoreInt: Int = (hGoals.times(3)) + hPoints
    private val _totalAwayScoreInt: Int = (aGoals.times(3)) + aPoints

    val homeScore: String = "${hGoals.twoDigitFormat()}:${hPoints.twoDigitFormat()}"
    val awayScore: String = "${aGoals.twoDigitFormat()}:${aPoints.twoDigitFormat()}"

    val totalHomeScore: String = "$_totalHomeScoreInt"
    val totalAwayScore: String = "$_totalAwayScoreInt"

    val homeDiff: String = "(${_totalHomeScoreInt - _totalAwayScoreInt})"
    val awayDiff: String = "(${_totalAwayScoreInt - _totalHomeScoreInt})"


    val homeColor: Color
        get() = when {
            _totalHomeScoreInt == _totalAwayScoreInt -> Color.Black
            _totalHomeScoreInt > _totalAwayScoreInt -> Color.Green
            else -> Color.Red
        }

    val awayColor: Color
        get() = when {
            _totalHomeScoreInt == _totalAwayScoreInt -> Color.Black
            _totalHomeScoreInt < _totalAwayScoreInt -> Color.Green
            else -> Color.Red
        }

    private fun Int.twoDigitFormat(): String = "%02d".format(this)

}
