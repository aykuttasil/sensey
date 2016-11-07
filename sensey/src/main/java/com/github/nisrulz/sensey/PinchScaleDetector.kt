package com.github.nisrulz.sensey

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector

/**
 * The type Pinch scale detector.
 */
class PinchScaleDetector
/**
 * Instantiates a new Pinch scale detector.

 * @param context
 * *     the context
 * *
 * @param pinchScaleListener
 * *     the pinch scale listener
 */
(context: Context, private val pinchScaleListener: PinchScaleDetector.PinchScaleListener) {

  private val scaleGestureDetector: ScaleGestureDetector

  init {
    scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener())
  }

  /**
   * On touch event boolean.

   * @param e
   * *     the e
   * *
   * @return the boolean
   */
  internal fun onTouchEvent(e: MotionEvent): Boolean {
    return scaleGestureDetector.onTouchEvent(e)
  }

  /**
   * The interface Pinch scale listener.
   */
  interface PinchScaleListener {
    /**
     * On scale.

     * @param scaleGestureDetector
     * *     the scale gesture detector
     * *
     * @param isScalingOut
     * *     the is scaling out
     */
    fun onScale(scaleGestureDetector: ScaleGestureDetector, isScalingOut: Boolean)

    /**
     * On scale start.

     * @param scaleGestureDetector
     * *     the scale gesture detector
     */
    fun onScaleStart(scaleGestureDetector: ScaleGestureDetector)

    /**
     * On scale end.

     * @param scaleGestureDetector
     * *     the scale gesture detector
     */
    fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector)
  }

  private inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {

    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {

      val scaleFactor = scaleGestureDetector.scaleFactor
      if (scaleFactor > 1) {
        pinchScaleListener.onScale(scaleGestureDetector, true)
      } else {
        pinchScaleListener.onScale(scaleGestureDetector, false)
      }
      return true
    }

    override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
      pinchScaleListener.onScaleStart(scaleGestureDetector)
      return true
    }

    override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
      pinchScaleListener.onScaleEnd(scaleGestureDetector)
    }
  }
}
