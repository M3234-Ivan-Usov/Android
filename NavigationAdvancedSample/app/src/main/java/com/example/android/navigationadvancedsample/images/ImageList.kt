package com.example.android.navigationadvancedsample.images

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R


class ImageList : Fragment() {
    companion object {
        lateinit var imageList: List<String>
        lateinit var imagePreview: ImageButton
        var currentImage: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageList = loadContent()
        val saved = savedInstanceState?.getInt("index")
        if (saved != null) {
            currentImage = saved

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_imagelist, container, false)
        view.findViewById<RecyclerView>(R.id.images_list).run {
            adapter = ImageAdapter(this@ImageList)
        }
        imagePreview = view.findViewById(R.id.image_preview)
        if (currentImage != -1) {
            imagePreview.setImageURI(imageList[currentImage].toUri())
        }
        imagePreview.setOnClickListener {
            if (currentImage != -1) {
                findNavController().navigate(R.id.action_image_to_fullscreen,
                        bundleOf("index" to currentImage))
            }
        }
        (activity as MainActivity).changeBottom(R.color.darkGrey)
        return view
    }

    private fun loadContent(): List<String> {
        val ctx = context ?: return emptyList()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val sortBy = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val list = ArrayList<String>()
        ctx.contentResolver.query(
                uri,
                projection,
                null,
                null,
                sortBy
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val absolutePathOfImage = cursor.getString(columnIndexData)
                list.add(absolutePathOfImage)
            }
        }
        return list
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", currentImage)
    }

}