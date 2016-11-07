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

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

/**
 * The type Sensor detector.
 */
abstract class SensorDetector(vararg sensorTypes: Int) : SensorEventListener {

  val sensorTypes: IntArray

  init {
    this.sensorTypes = sensorTypes
  }

  override fun onSensorChanged(sensorEvent: SensorEvent) {
    if (isSensorEventBelongsToPluggedTypes(sensorEvent)) {
      onSensorEvent(sensorEvent)
    }
  }

  override fun onAccuracyChanged(sensor: Sensor, i: Int) {
  }

  private fun isSensorEventBelongsToPluggedTypes(sensorEvent: SensorEvent): Boolean {
    for (sensorType in sensorTypes) {
      if (sensorEvent.sensor.type == sensorType) {
        return true
      }
    }

    return false
  }

  internal open fun onSensorEvent(sensorEvent: SensorEvent) {

  }
}
