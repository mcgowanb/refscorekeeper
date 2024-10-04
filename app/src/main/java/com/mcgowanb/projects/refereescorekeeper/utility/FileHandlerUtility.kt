package com.mcgowanb.projects.refereescorekeeper.utility

import android.content.Context
import com.google.gson.Gson

class FileHandlerUtility(private val context: Context, private val gson: Gson) {
    fun <T> saveState(state: T, fileName: String) {
        val jsonString = gson.toJson(state)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun <T> loadState(fileName: String, classOfT: Class<T>): T? {
        return try {
            context.openFileInput(fileName).bufferedReader().use {
                gson.fromJson(it, classOfT)
            }
        } catch (e: Exception) {
            null
        }
    }
}