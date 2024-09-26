package com.mcgowanb.projects.refereescorekeeper.utility

import android.content.Context
import android.media.MediaPlayer

class SoundUtility(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(soundResourceId: Int) {
        // Release any existing MediaPlayer
        mediaPlayer?.release()

        // Create and prepare a new MediaPlayer
        mediaPlayer = MediaPlayer.create(context, soundResourceId)

        mediaPlayer?.setOnCompletionListener { mp ->
            mp.release()
        }

        // Play the sound
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}