package com.mcgowanb.projects.refereescorekeeper.model

import com.google.gson.annotations.Expose

data class TimerState(
    @Expose
    val remainingTime: Int,
    @Expose
    val isRunning: Boolean,
    @Expose
    val lastPausedTime: Long = 0,
    @Expose
    val defaultMinutes: Int
)
