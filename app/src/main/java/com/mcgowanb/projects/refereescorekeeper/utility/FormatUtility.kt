package com.mcgowanb.projects.refereescorekeeper.utility

import com.mcgowanb.projects.refereescorekeeper.model.GameState

class FormatUtility {
    companion object {
        fun formatGameIntervals(gameState: GameState): String =
            "${gameState.elapsedPeriods}/${gameState.periods}"
    }
}