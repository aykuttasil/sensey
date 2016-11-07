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

import android.hardware.Sensor.TYPE_LIGHT

/**
 * The type Light detector.
 */
class LightDetector
/**
 * Instantiates a new Light detector.

 * @param threshold
 * *     the threshold
 * *
 * @param lightListener
 * *     the light listener
 */
(private val threshold: Float, private val lightListener: LightDetector.LightListener) : SensorDetector(
    TYPE_LIGHT) {

  /**
   * Instantiates a new Light detector.

   * @param lightListener
   * *     the light listener
   */
  constructor(lightListener: LightListener) : this(3f, lightListener) {
  }

  override fun onSensorEvent(sensorEvent: SensorEvent) {
    val lux = sensorEvent.values[0]
    if (lux < threshold) {
      // Dark
      lightListener.onDark()
    } else {
      // Not Dark
      lightListener.onLight()
    }
  }

  /**
   * The interface Light listener.
   */
  interface LightListener {
    /**
     * On dark.
     */
    fun onDark()

    /**
     * On light.
     */
    fun onLight()
  }
}
