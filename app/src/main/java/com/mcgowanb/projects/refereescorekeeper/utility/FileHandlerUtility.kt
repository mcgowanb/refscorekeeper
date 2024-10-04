package com.mcgowanb.projects.refereescorekeeper.utility

import android.content.Context
import com.google.gson.Gson
import com.mcgowanb.projects.refereescorekeeper.model.TimerState

class FileHandlerUtility(private val context: Context, private val gson: Gson) {
    private val fileName = "timer_state.json"

    fun saveTimerState(timerState: TimerState) {
        val jsonString = gson.toJson(timerState)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun loadTimerState(): TimerState? {
        return try {
            context.openFileInput(fileName).bufferedReader().use {
                gson.fromJson(it, TimerState::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }
}