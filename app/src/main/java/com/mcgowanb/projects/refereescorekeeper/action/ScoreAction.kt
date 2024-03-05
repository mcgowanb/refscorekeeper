package com.mcgowanb.projects.refereescorekeeper.action

sealed class ScoreAction() {
    data object AddHomePoint : ScoreAction()
    data object SubtractHomePoint : ScoreAction()
    data object AddHomeGoal : ScoreAction()
    data object AddAwayPoint : ScoreAction()
    data object AddAwayGoal : ScoreAction()
    data object Reset : ScoreAction()
//    data class Operation(val operation: ScoreOperation) : ScoreAction()
}