package com.example.android.navigationadvancedsample.audio

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R
import java.lang.RuntimeException

class AudioList : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    companion object {
        lateinit var audioList : List<Pair<String, String>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioList = loadContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        audioAdapter = AudioAdapter(this)
        val view: View = inflater.inflate(R.layout.fragment_audiolist, container, false)
        recycler = view.findViewById(R.id.audiolist_recycler)
        recycler.adapter = audioAdapter
        (activity as MainActivity).changeBottom(R.color.brightWhite)
        return view
    }


    private fun loadContent(): List<Pair<String, String>> {
        val ctx = context ?: return emptyList()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Audio.Media.TITLE)
        val sortBy = MediaStore.Audio.Media.TITLE
        val list = ArrayList<Pair<String, String>>()
        ctx.contentResolver.query(
                uri,
                projection,
                null,
                null,
                sortBy
        )?.use { cursor ->
            cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val title = cursor.getString(1)
                list.add(Pair(path, title))
            }
        }
        return list
    }
}
