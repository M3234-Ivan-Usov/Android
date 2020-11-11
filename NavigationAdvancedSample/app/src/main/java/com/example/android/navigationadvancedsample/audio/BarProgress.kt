package com.example.android.navigationadvancedsample.audio

import android.media.MediaPlayer
import android.widget.SeekBar

class BarProgress(val player: MediaPlayer) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        if (p2) {
            player.seekTo(p1)
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}