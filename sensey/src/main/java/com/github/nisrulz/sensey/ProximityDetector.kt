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

import android.hardware.Sensor.TYPE_PROXIMITY

/**
 * The type Proximity detector.
 */
class ProximityDetector
/**
 * Instantiates a new Proximity detector.

 * @param threshold
 * *     the threshold
 * *
 * @param proximityListener
 * *     the proximity listener
 */
(private val threshold: Float, private val proximityListener: ProximityDetector.ProximityListener) : SensorDetector(
    TYPE_PROXIMITY) {

  /**
   * Instantiates a new Proximity detector.

   * @param proximityListener
   * *     the proximity listener
   */
  constructor(proximityListener: ProximityListener) : this(3f, proximityListener) {
  }

  override fun onSensorEvent(sensorEvent: SensorEvent) {
    val distance = sensorEvent.values[0]
    if (distance < threshold) {
      proximityListener.onNear()
    } else {
      proximityListener.onFar()
    }
  }

  /**
   * The interface Proximity listener.
   */
  interface ProximityListener {
    /**
     * On near.
     */
    fun onNear()

    /**
     * On far.
     */
    fun onFar()
  }
}
