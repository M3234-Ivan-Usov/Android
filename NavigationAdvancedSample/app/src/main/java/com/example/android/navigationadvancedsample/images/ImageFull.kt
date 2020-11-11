package com.example.android.navigationadvancedsample.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R

class ImageFull : Fragment() {
    lateinit var imageSrc : ImageView
    var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = savedInstanceState?.getInt("index") ?: arguments?.getInt("index")!!
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image_full, container, false)
        imageSrc = view.findViewById(R.id.image_full_src)
        imageSrc.setImageURI(ImageList.imageList[index].toUri())
        view.setOnTouchListener(SwipeDetector(this))
        return view
    }

    override fun onResume() {
        super.onResume()
        with(activity as MainActivity) {
            supportActionBar?.hide()
            bottomNavigationView?.visibility = View.GONE
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", index)
    }

    override fun onDestroy() {
        super.onDestroy()
        with(ImageList) {
            currentImage = index
            imagePreview.setImageURI(imageList[index].toUri())
        }
        with(activity as MainActivity) {
            supportActionBar?.show()
            bottomNavigationView?.visibility = View.VISIBLE
        }
    }

}
