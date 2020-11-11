package app.com.animation.git;

import android.animation.ObjectAnimator
import android.os.AsyncTask
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
import app.com.animation.viewer.CodeViewFragment
import app.com.animation.viewer.CppParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier
import javax.net.ssl.HttpsURLConnection

class ConnectionFragment: Fragment() {
    lateinit var http: String
    var done = false
    lateinit var status: TextView
    lateinit var picture: ImageView
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
        status = view.findViewById(R.id.load_text)
        picture = view.findViewById(R.id.load_picture)
        getStatus()
        return view
    }

    override fun onStart() {
        super.onStart()
        if (!done) {
            AsyncDownload(this) {(activity as MainActivity).openViewer()}.execute()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).changeBottom(R.color.white_background)
    }


    fun getStatus() {
        if (done) {
            status.text = "Done"
            status.setTextColor(resources.getColor(R.color.local_file))
            picture.setImageDrawable(resources.getDrawable(R.drawable.ok))
            configureObjectAnimator("rotationY", AccelerateDecelerateInterpolator())
        }
        else {
            status.text = "Loading"
            configureObjectAnimator("rotation", LinearInterpolator())
        }
        AnimationUtils.loadAnimation(context,
            R.anim.my_animation
        ).also { animation ->
            status.startAnimation(animation)
        }
    }

    private fun configureObjectAnimator(property : String, interpol : Interpolator) {
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
