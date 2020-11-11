package com.example.android.navigationadvancedsample.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigationadvancedsample.R

class ImageAdapter(val fragment : Fragment) :
        RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    class ImageHolder(root: View, val picture : ImageButton = root.findViewById(R.id.image_src)) :
            RecyclerView.ViewHolder(root)

    val sizePx = fragment.resources.displayMetrics.widthPixels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_card, parent, false))
    }

    override fun getItemCount(): Int {
        return ImageList.imageList.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val current = ImageList.imageList[position].toUri()
        with(holder.picture) {
            layoutParams.height = sizePx / 4
            layoutParams.width = sizePx / 4
            setImageURI(current)
            setOnClickListener {
                ImageList.imagePreview.setImageURI(current)
                ImageList.currentImage = position
            }
        }

    }
}