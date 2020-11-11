package com.example.android.navigationadvancedsample.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R


class AudioAdapter(val fragment : Fragment) :
        RecyclerView.Adapter<AudioAdapter.AudioHolder>()  {
    class AudioHolder(root : View, val title : Button = root.findViewById(R.id.open_player)) :
            RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        return AudioHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.audio_card, parent, false))
    }

    override fun getItemCount(): Int {
        return AudioList.audioList.size
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        val current = AudioList.audioList[position]
        with(holder) {
            title.text = "${position + 1}. " + current.second
            title.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_audiolist_to_audio_player,
                        bundleOf("index" to position))
            }
        }
    }
}