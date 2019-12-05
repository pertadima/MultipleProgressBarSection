package id.co.multipleprogressbarsection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.core.content.ContextCompat

/**
 * Created by pertadima on 04,December,2019
 */

class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SeekBar(context, attrs) {

    private val listProgressItem = mutableListOf<ProgressItem>()
    private var roundedRadius: Float = 0F

    fun initData(listProgressItem: List<ProgressItem>, roundedRadius: Float = 25F) {
        this.listProgressItem.addAll(listProgressItem)
        this.roundedRadius = roundedRadius
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (listProgressItem.isNotEmpty()) {
            var progressItemWidth: Int
            var progressItemRight: Int
            val progressBarWidth = width
            val progressBarHeight = height - (TEXT_SIZE * 2).convertToDp()
            val thumbOffset = thumbOffset
            var lastProgressX = 0

            listProgressItem.forEachIndexed { index, item ->
                val progressPaint = Paint()
                progressPaint.color = ContextCompat.getColor(context, item.color)
                progressItemWidth = (item.percentage * progressBarWidth / 100).toInt()
                progressItemRight = lastProgressX + progressItemWidth

                if (index == listProgressItem.size - 1 && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth
                }

                val progressPath = roundedRect(
                    left = lastProgressX.toFloat(),
                    top = thumbOffset / HALF_SIZE,
                    right = progressItemRight.toFloat(),
                    bottom = (progressBarHeight - thumbOffset / HALF_SIZE),
                    roundedTopLeft = index == FIRST_INDEX,
                    roundedTopRight = index == listProgressItem.size - 1,
                    roundedBottomRight = index == listProgressItem.size - 1,
                    roundedBottomleft = index == FIRST_INDEX
                )

                with(canvas) {
                    drawPath(progressPath, progressPaint)
                    drawText(
                        "${item.percentage}%",
                        (lastProgressX + progressItemRight) / HALF_SIZE,
                        progressBarHeight + TEXT_SIZE * HALF_SIZE,
                        Paint().apply {
                            color = ContextCompat.getColor(context, android.R.color.black)
                            textAlign = Paint.Align.CENTER
                            textSize = TEXT_SIZE.convertToDp()
                        })
                }
                lastProgressX = progressItemRight
            }
        }
        super.onDraw(canvas)
    }

    private fun roundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        roundedTopLeft: Boolean,
        roundedTopRight: Boolean,
        roundedBottomRight: Boolean,
        roundedBottomleft: Boolean
    ): Path {
        var rx = roundedRadius
        var ry = roundedRadius

        val path = Path()
        if (rx < 0) rx = DEFAULT_VALUE
        if (ry < 0) ry = DEFAULT_VALUE
        val width = right - left
        val height = bottom - top
        if (rx > width / HALF_SIZE) rx = width / HALF_SIZE
        if (ry > height / HALF_SIZE) ry = height / HALF_SIZE
        val widthMinusCorners = width - HALF_SIZE * rx
        val heightMinusCorners = height - HALF_SIZE * ry
        path.moveTo(right, top + ry)

        //top-right corner
        if (roundedTopRight) path.rQuadTo(DEFAULT_VALUE, -ry, -rx, -ry)
        else {
            path.rLineTo(DEFAULT_VALUE, -ry)
            path.rLineTo(-rx, DEFAULT_VALUE)
        }
        path.rLineTo(-widthMinusCorners, DEFAULT_VALUE)

        //top-left corner
        if (roundedTopLeft) path.rQuadTo(-rx, DEFAULT_VALUE, -rx, ry)
        else {
            path.rLineTo(-rx, DEFAULT_VALUE)
            path.rLineTo(DEFAULT_VALUE, ry)
        }
        path.rLineTo(DEFAULT_VALUE, heightMinusCorners)

        //bottom-left corner
        if (roundedBottomleft) path.rQuadTo(DEFAULT_VALUE, ry, rx, ry)
        else {
            path.rLineTo(DEFAULT_VALUE, ry)
            path.rLineTo(rx, DEFAULT_VALUE)
        }
        path.rLineTo(widthMinusCorners, DEFAULT_VALUE)

        //bottom-right corner
        if (roundedBottomRight) path.rQuadTo(rx, DEFAULT_VALUE, rx, -ry)
        else {
            path.rLineTo(rx, DEFAULT_VALUE)
            path.rLineTo(DEFAULT_VALUE, -ry)
        }
        path.rLineTo(DEFAULT_VALUE, -heightMinusCorners)

        path.close() //Given close, last lineto can be removed.
        return path
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    private fun Float.convertToDp(): Float {
        val metrics = context.resources.displayMetrics
        val fPixels = metrics.density * this
        return (fPixels + 0.5f)
    }

    companion object {
        private const val DEFAULT_VALUE = 0F
        private const val FIRST_INDEX = 0
        private const val TEXT_SIZE = 12F
        private const val HALF_SIZE = 2F
    }
}