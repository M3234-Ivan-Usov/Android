package app.com.animation.git

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import app.com.animation.R

class MyView(context : Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private var alpha = 255
    private var step = 6
    var text = "Loading..."
    set(value) {
        field = value
        when (value) {
            "Loading..." -> {
                paint.color = resources.getColor(R.color.dark_background)
            }
            "Done"-> {
                paint.color = resources.getColor(R.color.local_file)
            }
            "Fail" -> {
                paint.color = resources.getColor(R.color.git_file)
            }
        }
        requestLayout()
    }
    init {
        paint.textSize = 100f
        paint.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(resources.getColor(R.color.white_background))
        paint.alpha = alpha
        alpha += step
        if (alpha < 0 || alpha > 255) {
            step = -step
            alpha += step
        }
        val textWidth = paint.measureText(text)
        val textX = (width - textWidth) / 2f
        val textY = height * 0.65f
        canvas?.drawText(text, textX, textY, paint)
        invalidate()
    }
}