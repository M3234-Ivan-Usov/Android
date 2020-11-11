package app.com.animation.storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import app.com.animation.MainActivity
import app.com.animation.R
import app.com.animation.viewer.CodeViewFragment
import app.com.animation.viewer.CppParser
import java.io.File

class FilesAdapter(private val main : MainActivity) : RecyclerView.Adapter<FilesAdapter.FileHolder>() {
    private var files = main.folder.list()!!

    class FileHolder(root: View,
                     val name: Button = root.findViewById(R.id.file_name),
                     val recycle : ImageButton = root.findViewById(R.id.recycle_button)) :
        RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        return FileHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.file_layout, parent, false))
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val name = files[position]
        with(holder.name) {
            text = name
            setOnClickListener {
                val content = File(main.folder, name).readText()
                val parser = CppParser(content, resources, name, false)
                parser.parse()
                with (CodeViewFragment) {
                    openSources.add(parser)
                    current = openSources.size - 1
                }
                main.openViewer()
            }
        }
        holder.recycle.setOnClickListener {
            val fm = main.supportFragmentManager
            DeleteFragment(File(main.folder, name)) { main.updateStorage()}.show(fm, "dialog")
        }
    }
}