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
import android.hardware.Sensor
import android.hardware.SensorManager
import android.view.MotionEvent
import com.github.nisrulz.sensey.FlipDetector.FlipListener
import com.github.nisrulz.sensey.LightDetector.LightListener
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import com.github.nisrulz.sensey.WaveDetector.WaveListener
import java.util.*

/**
 * The type Sensey.
 */
class Sensey private constructor() {

  /**
   * Map from any of default listeners (
   * [flipListener][FlipListener],
   * [lightListener][LightListener],
   * [orientationListener][OrientationListener]
   * [proximityListener][ProximityListener]
   * and [shakeListener][ShakeListener])
   * to SensorDetectors created by those listeners.

   * This map is needed to hold reference to all started detections **NOT**
   * through [Sensey.startSensorDetection], because the last one
   * passes task to hold reference of [sensorDetector][SensorDetector] to the client
   */
  private val defaultSensorsMap = HashMap<Any, SensorDetector>()
  private var sensorManager: SensorManager? = null
  private var touchTypeDetector: TouchTypeDetector? = null
  private var pinchScaleDetector: PinchScaleDetector? = null
  private var context: Context? = null

  private object LazyHolder {
    internal val INSTANCE = Sensey()
  }

  /**
   * Init.

   * @param context
   * *     the context
   */
  fun init(context: Context) {
    sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    this.context = context
  }

  private fun startLibrarySensorDetection(detector: SensorDetector, clientListener: Any) {
    if (!defaultSensorsMap.containsKey(clientListener)) {
      defaultSensorsMap.put(clientListener, detector)
      startSensorDetection(detector)
    }
  }

  /**
   * Start sensor detection.

   * @param detector
   * *     the detector
   */
  internal fun startSensorDetection(detector: SensorDetector) {
    val sensors = convertTypesToSensors(*detector.sensorTypes)
    if (areAllSensorsValid(sensors)) {
      registerDetectorForAllSensors(detector, sensors)
    }
  }

  private fun convertTypesToSensors(vararg sensorTypes: Int): Iterable<Sensor> {
    val sensors = ArrayList<Sensor>()
    for (sensorType in sensorTypes) {
      sensors.add(sensorManager!!.getDefaultSensor(sensorType))
    }
    return sensors
  }

  private fun areAllSensorsValid(sensors: Iterable<Sensor>): Boolean {
    for (sensor in sensors) {
      if (sensor == null) {
        return false
      }
    }

    return true
  }

  private fun registerDetectorForAllSensors(detector: SensorDetector, sensors: Iterable<Sensor>) {
    for (sensor in sensors) {
      sensorManager!!.registerListener(detector, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
  }

  /**
   * Stop sensor detection.

   * @param detector
   * *     the detector
   */
  internal fun stopSensorDetection(detector: SensorDetector?) {
    if (detector != null) {
      sensorManager!!.unregisterListener(detector)
    }
  }

  /**
   * Start shake detection.

   * @param shakeListener
   * *     the shake listener
   */
  fun startShakeDetection(shakeListener: ShakeListener) {
    startLibrarySensorDetection(ShakeDetector(shakeListener), shakeListener)
  }

  /**
   * Start shake detection.

   * @param threshold
   * *     the threshold
   * *
   * @param shakeListener
   * *     the shake listener
   */
  fun startShakeDetection(threshold: Int, shakeListener: ShakeListener) {
    startLibrarySensorDetection(ShakeDetector(threshold.toFloat(), shakeListener), shakeListener)
  }

  /**
   * Stop shake detection.

   * @param shakeListener
   * *     the shake listener
   */
  fun stopShakeDetection(shakeListener: ShakeListener) {
    stopLibrarySensorDetection(shakeListener)
  }

  private fun stopLibrarySensorDetection(clientListener: Any) {
    val detector = defaultSensorsMap.remove(clientListener)
    stopSensorDetection(detector)
  }

  /**
   * Start light detection.

   * @param lightListener
   * *     the light listener
   */
  fun startLightDetection(lightListener: LightListener) {
    startLibrarySensorDetection(LightDetector(lightListener), lightListener)
  }

  /**
   * Start light detection.

   * @param threshold
   * *     the threshold
   * *
   * @param lightListener
   * *     the light listener
   */
  fun startLightDetection(threshold: Int, lightListener: LightListener) {
    startLibrarySensorDetection(LightDetector(threshold.toFloat(), lightListener), lightListener)
  }

  /**
   * Stop light detection.

   * @param lightListener
   * *     the light listener
   */
  fun stopLightDetection(lightListener: LightListener) {
    stopLibrarySensorDetection(lightListener)
  }

  /**
   * Start flip detection.

   * @param flipListener
   * *     the flip listener
   */
  fun startFlipDetection(flipListener: FlipListener) {
    startLibrarySensorDetection(FlipDetector(flipListener), flipListener)
  }

  /**
   * Stop flip detection.

   * @param flipListener
   * *     the flip listener
   */
  fun stopFlipDetection(flipListener: FlipListener) {
    stopLibrarySensorDetection(flipListener)
  }

  /**
   * Start orientation detection.

   * @param orientationListener
   * *     the orientation listener
   */
  fun startOrientationDetection(orientationListener: OrientationListener) {
    startLibrarySensorDetection(OrientationDetector(orientationListener), orientationListener)
  }

  /**
   * Start orientation detection.

   * @param smoothness
   * *     the smoothness
   * *
   * @param orientationListener
   * *     the orientation listener
   */
  fun startOrientationDetection(smoothness: Int, orientationListener: OrientationListener) {
    startLibrarySensorDetection(OrientationDetector(smoothness, orientationListener),
        orientationListener)
  }

  /**
   * Stop orientation detection.

   * @param orientationListener
   * *     the orientation listener
   */
  fun stopOrientationDetection(orientationListener: OrientationListener) {
    stopLibrarySensorDetection(orientationListener)
  }

  /**
   * Start proximity detection.

   * @param proximityListener
   * *     the proximity listener
   */
  fun startProximityDetection(proximityListener: ProximityListener) {
    startLibrarySensorDetection(ProximityDetector(proximityListener), proximityListener)
  }

  /**
   * Start proximity detection.

   * @param threshold
   * *     the threshold
   * *
   * @param proximityListener
   * *     the proximity listener
   */
  fun startProximityDetection(threshold: Float, proximityListener: ProximityListener) {
    startLibrarySensorDetection(ProximityDetector(threshold, proximityListener),
        proximityListener)
  }

  /**
   * Stop proximity detection.

   * @param proximityListener
   * *     the proximity listener
   */
  fun stopProximityDetection(proximityListener: ProximityListener) {
    stopLibrarySensorDetection(proximityListener)
  }

  /**
   * Start proximity detection.

   * @param waveListener
   * *     the wave listener
   */
  fun startWaveDetection(waveListener: WaveListener) {
    startLibrarySensorDetection(WaveDetector(waveListener), waveListener)
  }

  /**
   * Start proximity detection.

   * @param threshold
   * *     the threshold
   * *
   * @param waveListener
   * *     the wave listener
   */
  fun startWaveDetection(threshold: Float, waveListener: WaveListener) {
    startLibrarySensorDetection(WaveDetector(threshold, waveListener), waveListener)
  }

  /**
   * Stop proximity detection.

   * @param waveListener
   * *     the wave listener
   */
  fun stopWaveDetection(waveListener: WaveListener) {
    stopLibrarySensorDetection(waveListener)
  }

  /**
   * Start pinch scale detection.

   * @param pinchScaleListener
   * *     the pinch scale listener
   */
  fun startPinchScaleDetection(pinchScaleListener: PinchScaleDetector.PinchScaleListener?) {
    if (pinchScaleListener != null) {
      pinchScaleDetector = PinchScaleDetector(context!!, pinchScaleListener)
    }
  }

  /**
   * Stop pinch scale detection.
   */
  fun stopPinchScaleDetection() {
    pinchScaleDetector = null
  }

  /**
   * Start touch type detection.

   * @param touchTypListener
   * *     the touch typ listener
   */
  fun startTouchTypeDetection(touchTypListener: TouchTypeDetector.TouchTypListener?) {
    if (touchTypListener != null) {
      touchTypeDetector = TouchTypeDetector(context!!, touchTypListener)
    }
  }

  /**
   * Stop touch type detection.
   */
  fun stopTouchTypeDetection() {
    touchTypeDetector = null
  }

  /**
   * Sets dispatch touch event.

   * @param event
   * *     the event
   */
  fun setupDispatchTouchEvent(event: MotionEvent) {
    if (touchTypeDetector != null) {
      touchTypeDetector!!.onTouchEvent(event)
    }

    if (pinchScaleDetector != null) {
      pinchScaleDetector!!.onTouchEvent(event)
    }
  }

  companion object {

    /**
     * Gets instance.

     * @return the instance
     */
    val instance: Sensey
      get() = LazyHolder.INSTANCE
  }
}
