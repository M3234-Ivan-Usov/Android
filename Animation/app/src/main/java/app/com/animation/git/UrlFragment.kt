package app.com.animation.git

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import app.com.animation.MainActivity
import app.com.animation.R

class UrlFragment : Fragment() {
    private lateinit var link : EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.url_layout, container, false)
        (activity as MainActivity).changeBottom(R.color.white_background)
        view.findViewById<Button>(R.id.search_url).run {
            setOnClickListener {
                val url = link.text.toString()
                if (url.isNotEmpty()) {
                    findNavController().navigate(
                        R.id.action_url_to_connection,
                        bundleOf("url" to url))
                }
            }
        }
        view.findViewById<Button>(R.id.clear_url).run {
            setOnClickListener {
                link.setText("")
            }
        }
        link = view.findViewById(R.id.link)
        link.setTextIsSelectable(true)
        return view
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).changeBottom(R.color.white_background)
    }
}