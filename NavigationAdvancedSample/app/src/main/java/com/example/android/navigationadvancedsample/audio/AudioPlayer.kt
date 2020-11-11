package com.example.android.navigationadvancedsample.audio

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class AudioPlayer : Fragment() {
    private lateinit var seekBar: SeekBar
    private lateinit var time: TextView
    private lateinit var progressHandler: AudioHandler
    private lateinit var player : MediaPlayer
    private lateinit var playerView: View
    private var hasAudio = AtomicBoolean(false)
    private var index: Int = -1
    private var progress: Thread? = null

    inner class AudioHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (hasAudio.get()) {
                val position = TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong())
                time.text = String.format("%02d:%02d", position / 60, position % 60)
                seekBar.progress = player.currentPosition
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = MediaPlayer()
        if (savedInstanceState != null) {
            configurePlayer(savedInstanceState.getInt("index"),
                    time = savedInstanceState.getInt("time"),
                    isPlaying = savedInstanceState.getBoolean("isPlaying"))
        } else {
            configurePlayer(arguments?.getInt("index") ?: -1, 0, true)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        playerView = inflater.inflate(R.layout.fragment_audioplayer, container, false)
        configureButtons()
        configureView()
        (activity as MainActivity).changeBottom(R.color.brightWhite)
        return playerView
    }

    override fun onResume() {
        super.onResume()
        progressHandler = AudioHandler()
        progress = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    progressHandler.sendMessage(progressHandler.obtainMessage())
                    Thread.sleep(500)
                }
            } catch (e: InterruptedException) {
            }
        }
        progress!!.start()
    }

    private fun configurePlayer(index: Int, time: Int = 0, isPlaying: Boolean = true) {
        this.index = index
        with(player) {
            setDataSource(AudioList.audioList[index].first)
            prepare()
            seekTo(time)
            setOnCompletionListener {
                playerView.findViewById<Button>(R.id.audio_nextButton).performClick()
            }
        }
        if (isPlaying) {
            player.start()
        }
        hasAudio.set(true)
    }

    private fun configureView() {
        seekBar = playerView.findViewById(R.id.audio_progress)
        seekBar.max = player.duration
        seekBar.setOnSeekBarChangeListener(BarProgress(player))
        time = playerView.findViewById(R.id.audio_current)
        playerView.findViewById<TextView>(R.id.audio_title).text = AudioList.audioList[index].second
        val total = TimeUnit.MILLISECONDS.toSeconds(player.duration.toLong())
        playerView.findViewById<TextView>(R.id.audio_duration).text = String.format("%02d:%02d", total / 60, total % 60)
    }

    private fun configureButtons() {
        playerView.findViewById<Button>(R.id.audio_playButton).run {
            setOnClickListener {
                if (!player.isPlaying) {
                    player.start()
                }
            }
        }
        playerView.findViewById<Button>(R.id.audio_pauseButton).run {
            setOnClickListener {
                if (player.isPlaying) {
                    player.pause()
                }
            }
        }
        playerView.findViewById<Button>(R.id.audio_stopButton).run {
            setOnClickListener {
                player.pause()
                player.seekTo(0)
            }
        }
        playerView.findViewById<Button>(R.id.audio_prevButton).run {
            setOnClickListener {
                val prev = if (index == 0) AudioList.audioList.size - 1 else index - 1
                hasAudio.set(false)
                player.reset()
                configurePlayer(prev)
                configureView()

            }
        }
        playerView.findViewById<Button>(R.id.audio_nextButton).run {
            setOnClickListener {
                val next = if (index == AudioList.audioList.size - 1) 0 else index + 1
                hasAudio.set(false)
                player.reset()
                configurePlayer(next)
                configureView()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("time", player.currentPosition)
        outState.putInt("index", index)
        outState.putBoolean("isPlaying", player.isPlaying)
    }

    override fun onDestroy() {
        hasAudio.set(false)
        progress?.interrupt()
        player.release()
        super.onDestroy()
    }
}
