package com.mcgowanb.projects.refereescorekeeper.model

data class GameState(
    var hPoints: Int = 13,
    var hGoals: Int = 4,
    var aPoints: Int = 21,
    var aGoals: Int = 10
) {
    val homeScore: String = String.format("%02d", hGoals) + ":" + String.format("%02d", hPoints)
    val awayScore: String = String.format("%02d", aGoals) + ":" + String.format("%02d", aPoints)

    val totalHomeScore: Int = (hGoals * 3) + hPoints
    val totalAwayScore: Int = (aGoals * 3) + aPoints
}
