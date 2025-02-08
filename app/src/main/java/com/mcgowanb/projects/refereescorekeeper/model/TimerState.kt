package com.mcgowanb.projects.refereescorekeeper.model

import com.google.gson.annotations.Expose

data class TimerState(
    @Expose
    val remainingTime: Int,
    @Expose
    val isRunning: Boolean,
    @Expose
    val formattedTime: String,
    @Expose
    val formattedElapsedTime: String,
    @Expose
    val isOvertime: Boolean,
    @Expose
    val overtimeSeconds: Int,
    @Expose
    val currentPeriod: Int,
    @Expose
    val defaultMinutes: Int
)
