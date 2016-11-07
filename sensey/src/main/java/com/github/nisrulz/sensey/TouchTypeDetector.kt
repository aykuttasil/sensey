/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nisrulz.sensey

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * The type Touch type detector.
 */
class TouchTypeDetector
/**
 * Instantiates a new Touch type detector.

 * @param context
 * *     the context
 * *
 * @param touchTypListener
 * *     the touch typ listener
 */
(context: Context, private val touchTypListener: TouchTypeDetector.TouchTypListener) {
  /**
   * The Gesture listener.
   */
  internal val gestureListener: GestureListener // it's needed for TouchTypeDetectorTest, don't remove
  //gesture detector
  private val gDetect: GestureDetectorCompat

  init {
    gestureListener = GestureListener()
    gDetect = GestureDetectorCompat(context, gestureListener)
  }

  /**
   * On touch event boolean.

   * @param event
   * *     the event
   * *
   * @return the boolean
   */
  internal fun onTouchEvent(event: MotionEvent): Boolean {

    when (event.actionMasked) {
      MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount == 3) {
        touchTypListener.onThreeFingerSingleTap()
      } else if (event.pointerCount == 2) {
        touchTypListener.onTwoFingerSingleTap()
      }
    }
    return gDetect.onTouchEvent(event)
  }

  /**
   * The interface Touch typ listener.
   */
  interface TouchTypListener {

    /**
     * On two finger single tap.
     */
    fun onTwoFingerSingleTap()

    /**
     * On three finger single tap.
     */
    fun onThreeFingerSingleTap()

    /**
     * On double tap.
     */
    fun onDoubleTap()

    /**
     * On scroll.

     * @param scrollDirection
     * *     the scroll direction
     */
    fun onScroll(scrollDirection: Int)

    /**
     * On single tap.
     */
    fun onSingleTap()

    /**
     * On swipe.

     * @param swipeDirection
     * *     the swipe direction
     */
    fun onSwipe(swipeDirection: Int)

    /**
     * On long press.
     */
    fun onLongPress()
  }

  /**
   * The type Gesture listener.
   */
  internal inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapUp(e: MotionEvent): Boolean {
      return super.onSingleTapUp(e)
    }

    override fun onLongPress(e: MotionEvent) {
      touchTypListener.onLongPress()
      super.onLongPress(e)
    }

    override fun onScroll(startevent: MotionEvent, finishevent: MotionEvent, distanceX: Float,
        distanceY: Float): Boolean {

      val deltaX = finishevent.x - startevent.x
      val deltaY = finishevent.y - startevent.y

      if (Math.abs(deltaX) > Math.abs(deltaY)) {
        //Scrolling Horizontal
        if (Math.abs(deltaX) > SWIPE_MIN_DISTANCE) {
          if (deltaX > 0) {
            touchTypListener.onScroll(SCROLL_DIR_RIGHT)
          } else {
            touchTypListener.onScroll(SCROLL_DIR_LEFT)
          }
        }
      } else {
        //Scrolling Vertical
        if (Math.abs(deltaY) > SWIPE_MIN_DISTANCE) {
          if (deltaY > 0) {
            touchTypListener.onScroll(SCROLL_DIR_DOWN)
          } else {
            touchTypListener.onScroll(SCROLL_DIR_UP)
          }
        }
      }

      return super.onScroll(startevent, finishevent, distanceX, distanceY)
    }

    override fun onFling(startevent: MotionEvent, finishevent: MotionEvent, velocityX: Float,
        velocityY: Float): Boolean {

      val deltaX = finishevent.x - startevent.x
      val deltaY = finishevent.y - startevent.y

      if (Math.abs(deltaX) > Math.abs(deltaY)) {
        if (Math.abs(deltaX) > SWIPE_MIN_DISTANCE && Math.abs(
            velocityX) > SWIPE_THRESHOLD_VELOCITY) {
          if (deltaX > 0) {
            touchTypListener.onSwipe(SWIPE_DIR_RIGHT)
          } else {
            touchTypListener.onSwipe(SWIPE_DIR_LEFT)
          }
        }
      } else {
        if (Math.abs(deltaY) > SWIPE_MIN_DISTANCE && Math.abs(
            velocityY) > SWIPE_THRESHOLD_VELOCITY) {
          if (deltaY > 0) {
            touchTypListener.onSwipe(SWIPE_DIR_DOWN)
          } else {
            touchTypListener.onSwipe(SWIPE_DIR_UP)
          }
        }
      }

      return false
    }

    override fun onDown(e: MotionEvent): Boolean {
      return super.onDown(e)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
      touchTypListener.onDoubleTap()
      return super.onDoubleTap(e)
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
      touchTypListener.onSingleTap()
      return super.onSingleTapConfirmed(e)
    }
  }

  companion object {

    /**
     * The constant SCROLL_DIR_UP.
     */
    val SCROLL_DIR_UP = 1
    /**
     * The constant SCROLL_DIR_RIGHT.
     */
    val SCROLL_DIR_RIGHT = 2
    /**
     * The constant SCROLL_DIR_DOWN.
     */
    val SCROLL_DIR_DOWN = 3
    /**
     * The constant SCROLL_DIR_LEFT.
     */
    val SCROLL_DIR_LEFT = 4

    /**
     * The constant SWIPE_DIR_UP.
     */
    val SWIPE_DIR_UP = 5
    /**
     * The constant SWIPE_DIR_RIGHT.
     */
    val SWIPE_DIR_RIGHT = 6
    /**
     * The constant SWIPE_DIR_DOWN.
     */
    val SWIPE_DIR_DOWN = 7
    /**
     * The constant SWIPE_DIR_LEFT.
     */
    val SWIPE_DIR_LEFT = 8

    val SWIPE_MIN_DISTANCE = 120
    val SWIPE_THRESHOLD_VELOCITY = 200
  }
}