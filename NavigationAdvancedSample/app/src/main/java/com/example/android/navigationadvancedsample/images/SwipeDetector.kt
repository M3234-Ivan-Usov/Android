package com.example.android.navigationadvancedsample.images

import android.view.MotionEvent
import android.view.View
import androidx.core.net.toUri

class SwipeDetector(val fragment: ImageFull) :
        View.OnTouchListener {
    private val minimal = 170.0f
    private var down = 0.0f
    private var up = 0.0f

    private fun leftToRight() {
        val next = if (fragment.index == ImageList.imageList.size - 1) 0 else fragment.index + 1
        fragment.index = next
        fragment.imageSrc.setImageURI(ImageList.imageList[next].toUri())
    }

    private fun rightToLeft() {
        val prev = if (fragment.index == 0) ImageList.imageList.size - 1 else fragment.index - 1
        fragment.index = prev
        fragment.imageSrc.setImageURI(ImageList.imageList[prev].toUri())
    }



    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    down = event.x
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    up = event.x
                    v?.performClick()
                    if (down - up > minimal) {
                        leftToRight()
                    }
                    if (up - down > minimal) {
                        rightToLeft()
                    }
                    return false
                }
                MotionEvent.ACTION_MOVE -> return true
            }
        }
        return false
    }
}
