package com.mcgowanb.projects.refereescorekeeper.presentation.model

data class TeamScore(
    var goals: Int = 0,
    var points: Int = 0
) {
    fun addPoint(): Int {
        points++
        return points
    }

    fun subtractPoint(): Int{
        points--
        return points
    }

    fun addGoal(): Int{
        goals++
        return goals
    }

    fun subtractGoal(): Int{
        goals--
        return goals
    }

    fun getTotalPoints(): Int {
        return points + (goals * 3)
    }
}
