package app.com.animation.git;

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.com.animation.MainActivity
import app.com.animation.R

class ConnectionFragment : Fragment() {
    lateinit var http: String
    lateinit var picture: ImageView
    lateinit var statusView: MyView
    private var objAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        http = arguments?.getString("url").toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.connection_layout, container, false)
        view.findViewById<TextView>(R.id.load_target).text = http
        statusView = view.findViewById(R.id.load_text)
        picture = view.findViewById(R.id.load_picture)
        updateStatus()
        return view
    }

    override fun onStart() {
        super.onStart()
        if (MainActivity.downloadStatus == 0) {
            AsyncDownload(this).execute()
        } else {
            updateStatus()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).changeBottom(R.color.white_background)
    }


    fun updateStatus() {
        when (MainActivity.downloadStatus) {
            1 -> {
                statusView.text = "Done"
                picture.setImageDrawable(resources.getDrawable(R.drawable.ok))
                configureObjectAnimator("rotationY", AccelerateDecelerateInterpolator())
            }
            -1 -> {
                statusView.text = "Fail"
                configureObjectAnimator("rotation", LinearInterpolator())
            }
            0 -> {
                statusView.text = "Loading..."
                configureObjectAnimator("rotation", LinearInterpolator())
            }
        }
    }

    private fun configureObjectAnimator(property: String, interpol: Interpolator) {
        objAnimator?.end()
        objAnimator = ObjectAnimator.ofFloat(picture, property, 0.0f, 360.0f).apply {
            duration = ANIMATION_DURATION
            repeatCount = ObjectAnimator.INFINITE
            interpolator = interpol
            start()
        }
    }

    companion object {
        const val CANCELLED = "Download was cancelled"
        const val EXCEPTED = "Download was failed"
        private const val ANIMATION_DURATION = 1000L
    }
}
