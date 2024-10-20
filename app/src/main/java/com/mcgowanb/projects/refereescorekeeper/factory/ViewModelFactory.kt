package com.mcgowanb.projects.refereescorekeeper.factory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.model.MatchReportViewModel
import com.mcgowanb.projects.refereescorekeeper.utility.FileHandlerUtility
import com.mcgowanb.projects.refereescorekeeper.utility.SoundUtility
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@RequiresApi(Build.VERSION_CODES.S)
class ViewModelFactory(
    private val fileHandler: FileHandlerUtility,
    private val vibrationUtility: VibrationUtility,
    private val soundUtility: SoundUtility
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GameTimeViewModel::class.java) -> {
                GameTimeViewModel(fileHandler, vibrationUtility, soundUtility) as T
            }

            modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                GameViewModel(fileHandler) as T
            }

            modelClass.isAssignableFrom(MatchReportViewModel::class.java) -> {
                MatchReportViewModel(fileHandler) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}