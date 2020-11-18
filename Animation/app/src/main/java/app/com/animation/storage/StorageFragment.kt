package app.com.animation.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.com.animation.MainActivity
import app.com.animation.R

class StorageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.storage_layout, container, false).apply {
            findViewById<RecyclerView>(R.id.file_recycler).adapter =
                FilesAdapter(activity as MainActivity)
            findViewById<TextView>(R.id.home_dir).text =
                (activity as MainActivity).folder.absolutePath
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).changeBottom(R.color.white_background)
    }
}