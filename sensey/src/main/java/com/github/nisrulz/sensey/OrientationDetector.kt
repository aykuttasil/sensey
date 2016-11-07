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

import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.media.ExifInterface

import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD

/**
 * The type Orientation detector.
 */
class OrientationDetector
/**
 * Instantiates a new Orientation detector.

 * @param smoothness
 * *     the smoothness
 * *
 * @param orientationListener
 * *     the orientation listener
 */
(private val smoothness: Int, private val orientationListener: OrientationDetector.OrientationListener) : SensorDetector(
    TYPE_ACCELEROMETER, TYPE_MAGNETIC_FIELD) {
  private val pitches: FloatArray
  private val rolls: FloatArray
  /**
   * The M gravity.
   */
  private var mGravity: FloatArray? = null
  /**
   * The M geomagnetic.
   */
  private var mGeomagnetic: FloatArray? = null
  private var averagePitch = 0f
  private var averageRoll = 0f
  private var orientation = ORIENTATION_PORTRAIT

  /**
   * Instantiates a new Orientation detector.

   * @param orientationListener
   * *     the orientation listener
   */
  constructor(orientationListener: OrientationListener) : this(1, orientationListener) {
  }

  init {

    pitches = FloatArray(smoothness)
    rolls = FloatArray(smoothness)
  }

  override fun onSensorEvent(event: SensorEvent) {
    if (event.sensor.type == TYPE_ACCELEROMETER) {
      mGravity = event.values
    }
    if (event.sensor.type == TYPE_MAGNETIC_FIELD) {
      mGeomagnetic = event.values
    }
    if (mGravity != null && mGeomagnetic != null) {
      val R = FloatArray(9)
      val I = FloatArray(9)
      val success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)
      if (success) {
        val orientationData = FloatArray(3)
        SensorManager.getOrientation(R, orientationData)
        averagePitch = addValue(orientationData[1], pitches)
        averageRoll = addValue(orientationData[2], rolls)
        orientation = calculateOrientation()
        when (orientation) {
          ORIENTATION_LANDSCAPE -> orientationListener.onRightSideUp()
          ORIENTATION_LANDSCAPE_REVERSE -> orientationListener.onLeftSideUp()
          ORIENTATION_PORTRAIT -> orientationListener.onTopSideUp()
          ORIENTATION_PORTRAIT_REVERSE -> orientationListener.onBottomSideUp()
          else -> {
          }
        }// do nothing
      }
    }
  }

  private fun addValue(value: Float, values: FloatArray): Float {
    val temp_value = Math.round(Math.toDegrees(value.toDouble())).toFloat()
    var average = 0f
    for (i in 1..smoothness - 1) {
      values[i - 1] = values[i]
      average += values[i]
    }
    values[smoothness - 1] = temp_value
    average = (average + temp_value) / smoothness
    return average
  }

  private fun calculateOrientation(): Int {
    // finding local orientation dip
    if ((orientation == ORIENTATION_PORTRAIT || orientation == ORIENTATION_PORTRAIT_REVERSE) && averageRoll > -30 && averageRoll < 30) {
      if (averagePitch > 0) {
        return ORIENTATION_PORTRAIT_REVERSE
      } else {
        return ORIENTATION_PORTRAIT
      }
    } else {
      // divides between all orientations
      if (Math.abs(averagePitch) >= 30) {
        if (averagePitch > 0) {
          return ORIENTATION_PORTRAIT_REVERSE
        } else {
          return ORIENTATION_PORTRAIT
        }
      } else {
        if (averageRoll > 0) {
          return ORIENTATION_LANDSCAPE_REVERSE
        } else {
          return ORIENTATION_LANDSCAPE
        }
      }
    }
  }

  /**
   * The interface Orientation listener.
   */
  interface OrientationListener {
    /**
     * On top side up.
     */
    fun onTopSideUp()

    /**
     * On bottom side up.
     */
    fun onBottomSideUp()

    /**
     * On right side up.
     */
    fun onRightSideUp()

    /**
     * On left side up.
     */
    fun onLeftSideUp()
  }

  companion object {

    private val ORIENTATION_PORTRAIT = ExifInterface.ORIENTATION_ROTATE_90 // 6
    private val ORIENTATION_LANDSCAPE_REVERSE = ExifInterface.ORIENTATION_ROTATE_180// 3
    private val ORIENTATION_LANDSCAPE = ExifInterface.ORIENTATION_NORMAL // 1
    private val ORIENTATION_PORTRAIT_REVERSE = ExifInterface.ORIENTATION_ROTATE_270 // 8
  }
}

