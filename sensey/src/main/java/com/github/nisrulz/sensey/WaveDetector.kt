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

import android.hardware.Sensor.TYPE_PROXIMITY
import android.hardware.SensorEvent

/**
 * The type Wave detector.
 */
class WaveDetector
/**
 * Instantiates a new Wave detector.

 * @param threshold
 * *     the threshold
 * *
 * @param waveListener
 * *     the wave listener
 */
(private val threshold: Float,
    /**
     * The Wave listener.
     */
    private val waveListener: WaveDetector.WaveListener) : SensorDetector(TYPE_PROXIMITY) {
  /**
   * The Last proximity event time.
   */
  private var lastProximityEventTime: Long = 0
  /**
   * The Proximity far.
   */
  private val proximityFar = 0
  /**
   * The Proximity near.
   */
  private val proximityNear = 1
  /**
   * The Last proximity state.
   */
  private var lastProximityState: Int = 0

  /**
   * Instantiates a new Wave detector.

   * @param waveListener
   * *     the wave listener
   */
  constructor(waveListener: WaveListener) : this(1000f, waveListener) {
  }

  override fun onSensorEvent(sensorEvent: SensorEvent) {
    val distance = sensorEvent.values[0]
    val proximityState: Int
    if (distance == 0f) {
      proximityState = proximityNear
    } else {
      proximityState = proximityFar
    }

    val now = System.currentTimeMillis()
    val eventDeltaMillis = now - this.lastProximityEventTime
    if (eventDeltaMillis < threshold
        && proximityNear == lastProximityState
        && proximityFar == proximityState) {

      // Wave detected
      waveListener.onWave()
    }
    this.lastProximityEventTime = now
    this.lastProximityState = proximityState
  }

  /**
   * The interface Wave listener.
   */
  interface WaveListener {
    /**
     * On wave.
     */
    fun onWave()
  }
}
