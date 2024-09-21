package com.mcgowanb.projects.refereescorekeeper.model

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.Expose
import com.mcgowanb.projects.refereescorekeeper.enums.GameStatus

data class GameState(
    @Expose
    val hPoints: Int = 0,
    @Expose
    val hGoals: Int = 0,
    @Expose
    val aPoints: Int = 0,
    @Expose
    val aGoals: Int = 0,
    @Expose
    val status: GameStatus = GameStatus.NOT_STARTED
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (hPoints != other.hPoints) return false
        if (hGoals != other.hGoals) return false
        if (aPoints != other.aPoints) return false
        if (aGoals != other.aGoals) return false
        if (_totalHomeScoreInt != other._totalHomeScoreInt) return false
        if (_totalAwayScoreInt != other._totalAwayScoreInt) return false
        if (homeScore != other.homeScore) return false
        if (awayScore != other.awayScore) return false
        if (totalHomeScore != other.totalHomeScore) return false
        if (totalAwayScore != other.totalAwayScore) return false
        if (homeDiff != other.homeDiff) return false
        if (awayDiff != other.awayDiff) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hPoints
        result = 31 * result + hGoals
        result = 31 * result + aPoints
        result = 31 * result + aGoals
        result = 31 * result + _totalHomeScoreInt
        result = 31 * result + _totalAwayScoreInt
        result = 31 * result + homeScore.hashCode()
        result = 31 * result + awayScore.hashCode()
        result = 31 * result + totalHomeScore.hashCode()
        result = 31 * result + totalAwayScore.hashCode()
        result = 31 * result + homeDiff.hashCode()
        result = 31 * result + awayDiff.hashCode()
        return result
    }


}
