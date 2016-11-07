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

import android.hardware.Sensor.TYPE_ACCELEROMETER

/**
 * The type Shake detector.
 */
class ShakeDetector
/**
 * Instantiates a new Shake detector.

 * @param threshold
 * *     the threshold
 * *
 * @param shakeListener
 * *     the shake listener
 */
(private val threshold: Float, private val shakeListener: ShakeDetector.ShakeListener) : SensorDetector(
    TYPE_ACCELEROMETER) {
  private var mAccel: Float = 0.toFloat()
  private var mAccelCurrent = SensorManager.GRAVITY_EARTH

  /**
   * Instantiates a new Shake detector.

   * @param shakeListener
   * *     the shake listener
   */
  constructor(shakeListener: ShakeListener) : this(3f, shakeListener) {
  }

  override fun onSensorEvent(sensorEvent: SensorEvent) {
    // Shake detection
    val x = sensorEvent.values[0]
    val y = sensorEvent.values[1]
    val z = sensorEvent.values[2]
    val mAccelLast = mAccelCurrent
    mAccelCurrent = Math.sqrt(x * x + y * y + z * z.toDouble()).toFloat()
    val delta = mAccelCurrent - mAccelLast
    mAccel = mAccel * 0.9f + delta
    // Make this higher or lower according to how much
    // motion you want to detect
    if (mAccel > threshold) {
      shakeListener.onShakeDetected()
    }
  }

  /**
   * The interface Shake listener.
   */
  interface ShakeListener {
    /**
     * On shake detected.
     */
    fun onShakeDetected()
  }
}
