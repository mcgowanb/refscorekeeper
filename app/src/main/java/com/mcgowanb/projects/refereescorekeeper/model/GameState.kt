package com.mcgowanb.projects.refereescorekeeper.model

data class GameState(
    var hPoints: Int = 0,
    var hGoals: Int = 0,
    var aPoints: Int = 0,
    var aGoals: Int = 0
) {
    val homeScore: String = String.format("%02d", hGoals) + ":" + String.format("%02d", hPoints)
    val awayScore: String = String.format("%02d", aGoals) + ":" + String.format("%02d", aPoints)

    val totalHomeScore: String = String.format("(%s)", (hGoals * 3) + hPoints)
    val totalAwayScore: String = String.format("(%s)", (aGoals * 3) + aPoints)
}
