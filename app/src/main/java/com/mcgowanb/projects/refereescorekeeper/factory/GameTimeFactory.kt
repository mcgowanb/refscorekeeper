package com.mcgowanb.projects.refereescorekeeper.factory

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@RequiresApi(Build.VERSION_CODES.S)
class GameTimeViewModelFactory(
    private val context: Context,
    private val gson: Gson,
    private val vibrationUtility: VibrationUtility
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameTimeViewModel::class.java)) {
            val fileHandler = FileHandlerUtility(context, gson)
            val soundUtility = SoundUtility(context)
            return GameTimeViewModel(fileHandler, vibrationUtility, soundUtility) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}