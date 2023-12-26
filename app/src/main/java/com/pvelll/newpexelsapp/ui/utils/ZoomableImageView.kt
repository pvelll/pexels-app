package com.pvelll.newpexelsapp.ui.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.LinearInterpolator

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var matrix: Matrix = Matrix()
    private var scaleFactor = 1f
    private val maxScale = 3.0f
    private val minScale = 1.0f
    private var isScaling = false
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    init {
        matrix = Matrix()
        scaleFactor = 1f
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        scaleType = ScaleType.MATRIX
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isScaling = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (scaleFactor > minScale && scaleFactor < maxScale) {
                    isScaling = true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isScaling) {
                    ScaleListener().resetImage()
                }
                isScaling = false
            }
        }

        return true
    }

    override fun onDraw(canvas: android.graphics.Canvas) {
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor)
        super.onDraw(canvas)
        canvas.restore()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private val lastTouchPoint = PointF()

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastTouchPoint.set(detector.focusX, detector.focusY)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = minScale.coerceAtLeast(scaleFactor.coerceAtMost(maxScale))
            matrix.setScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            matrix.postTranslate(
                detector.focusX - lastTouchPoint.x,
                detector.focusY - lastTouchPoint.y
            )
            lastTouchPoint.set(detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            super.onScaleEnd(detector)
            if (isScaling && scaleFactor > maxScale) {
                resetImage()
            }
        }

        fun resetImage() {
            val animator = ValueAnimator.ofFloat(scaleFactor, 1f)
            animator.duration = 300
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener { valueAnimator ->
                scaleFactor = valueAnimator.animatedValue as Float
                matrix.reset()
                matrix.setScale(scaleFactor, scaleFactor, width / 2f, height / 2f)
                imageMatrix = matrix
                invalidate()
            }
            animator.start()
        }
    }
}