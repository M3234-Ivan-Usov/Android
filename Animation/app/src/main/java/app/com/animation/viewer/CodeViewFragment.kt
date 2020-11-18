package app.com.animation.viewer;

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.com.animation.MainActivity
import app.com.animation.R
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.util.*

class CodeViewFragment : Fragment() {
    companion object {
        val openSources = LinkedList<CppParser>()
        var current = -1
    }

    private lateinit var tabs: TabLayout
    private lateinit var codeView: TextView
    private lateinit var lineView: TextView
    private lateinit var onStorage: HashSet<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.code_view_layout, container, false)
        onStorage = (activity as MainActivity).folder.list()!!.toHashSet()
        initViews(view)
        update(current)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).changeBottom(R.color.dark_background)
    }

    private fun initViews(view: View) {
        tabs = view.findViewById(R.id.tabs)
        codeView = view.findViewById(R.id.code_content)
        lineView = view.findViewById(R.id.lines)
        for (i in 0 until openSources.size) {
            openSources[i].fromGit = !onStorage.contains(openSources[i].name)
            tabs.addTab(tabs.newTab().apply {
                setCustomView(R.layout.tab_layout)
                customView!!.findViewById<TextView>(R.id.source_name).apply {
                    text = openSources[i].name
                    setTextColor(
                        if (openSources[i].fromGit)
                            resources.getColor(R.color.git_file) else
                            resources.getColor(R.color.local_file)
                    )
                }
                customView!!.findViewById<ImageButton>(R.id.save_tab).setOnClickListener {
                    if (openSources[position].fromGit) {
                        val file = File(
                            (activity as MainActivity).folder, openSources[position].name
                        )
                        if (!file.exists()) {
                            file.createNewFile()
                            file.writeText(openSources[position].raw)
                            tabs.getTabAt(position)!!.customView!!.findViewById<TextView>(
                                R.id.source_name
                            ).setTextColor(resources.getColor(R.color.local_file))
                            openSources[position].fromGit = false
                        }
                    }
                }
                customView!!.findViewById<ImageButton>(R.id.close_tab).setOnClickListener {
                    openSources.removeAt(position)
                    update(if (position < openSources.size) position else position - 1)
                    tabs.removeTab(this)
                }
            }, i == current)
        }
        tabs.addOnTabSelectedListener(TabListener(this))
    }

    private fun numLines(lines: Int): String {
        var numeration = ""
        for (line in 0..lines) {
            numeration += "${line + 1}\n"
        }
        return numeration
    }

    fun update(position: Int) {
        current = position
        if (current != -1) {
            codeView.text = openSources[current].parse()
            lineView.text = numLines(openSources[current].linesAmount)
        }
        else {
            codeView.text = resources.getString(R.string.empty_viewer)
            lineView.text = resources.getString(R.string.empty_numeration)
        }
    }
}
