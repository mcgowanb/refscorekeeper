package com.mcgowanb.projects.refereescorekeeper.model

import com.google.gson.annotations.Expose

data class MatchReport(
    @Expose val events: List<String> = listOf()
)
