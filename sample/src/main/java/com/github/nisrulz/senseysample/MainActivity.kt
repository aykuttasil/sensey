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

package com.github.nisrulz.senseysample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import com.github.nisrulz.sensey.*

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, ShakeDetector.ShakeListener, FlipDetector.FlipListener, LightDetector.LightListener, OrientationDetector.OrientationListener, ProximityDetector.ProximityListener, WaveDetector.WaveListener {

  private var txtResult: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Init Sensey
    Sensey.instance.init(this)

    txtResult = findViewById(R.id.textView_result) as TextView

    val swt1 = findViewById(R.id.Switch1) as SwitchCompat
    swt1.setOnCheckedChangeListener(this)
    swt1.isChecked = false

    val swt2 = findViewById(R.id.Switch2) as SwitchCompat
    swt2.setOnCheckedChangeListener(this)
    swt2.isChecked = false

    val swt3 = findViewById(R.id.Switch3) as SwitchCompat
    swt3.setOnCheckedChangeListener(this)
    swt3.isChecked = false

    val swt4 = findViewById(R.id.Switch4) as SwitchCompat
    swt4.setOnCheckedChangeListener(this)
    swt4.isChecked = false

    val swt5 = findViewById(R.id.Switch5) as SwitchCompat
    swt5.setOnCheckedChangeListener(this)
    swt5.isChecked = false

    val swt6 = findViewById(R.id.Switch6) as SwitchCompat
    swt6.setOnCheckedChangeListener(this)
    swt6.isChecked = false

    val btnTouchEvent = findViewById(R.id.btn_touchevent) as Button
    btnTouchEvent.setOnClickListener {
      startActivity(Intent(this@MainActivity, TouchActivity::class.java))
    }
  }

  override fun onCheckedChanged(switchbtn: CompoundButton, isChecked: Boolean) {
    when (switchbtn.id) {

      R.id.Switch1 -> if (isChecked) {
        Sensey.instance.startShakeDetection(10, this)
      } else {
        Sensey.instance.stopShakeDetection(this)
      }
      R.id.Switch2 -> if (isChecked) {
        Sensey.instance.startFlipDetection(this)
      } else {
        Sensey.instance.stopFlipDetection(this)
      }
      R.id.Switch3 -> if (isChecked) {
        Sensey.instance.startOrientationDetection(this)
      } else {
        Sensey.instance.stopOrientationDetection(this)
      }
      R.id.Switch4 -> if (isChecked) {
        Sensey.instance.startProximityDetection(this)
      } else {
        Sensey.instance.stopProximityDetection(this)
      }
      R.id.Switch5 -> if (isChecked) {
        Sensey.instance.startLightDetection(10, this)
      } else {
        Sensey.instance.stopLightDetection(this)
      }

      R.id.Switch6 -> if (isChecked) {
        Sensey.instance.startWaveDetection(this)
      } else {
        Sensey.instance.stopWaveDetection(this)
      }

      else -> {
      }
    }// Do nothing
  }

  override fun onPause() {
    super.onPause()
    // Stop Gesture Detections
    Sensey.instance.stopShakeDetection(this)
    Sensey.instance.stopFlipDetection(this)
    Sensey.instance.stopOrientationDetection(this)
    Sensey.instance.stopProximityDetection(this)
    Sensey.instance.stopLightDetection(this)
    Sensey.instance.stopWaveDetection(this)
  }

  override fun onFaceUp() {
    setResultTextView("Face UP")
  }

  private fun setResultTextView(text: String) {
    if (txtResult != null) {
      txtResult!!.text = text
      resetResultInView(txtResult!!)
      if (DEBUG) {
        Log.i(LOGTAG, text)
      }
    }
  }

  private fun resetResultInView(txt: TextView) {
    val handler = Handler()
    handler.postDelayed({ txt.text = "..Results show here..." }, 3000)
  }

  override fun onFaceDown() {
    setResultTextView("Face Down")
  }

  override fun onDark() {
    setResultTextView("Dark")
  }

  override fun onLight() {
    setResultTextView("Not Dark")
  }

  override fun onTopSideUp() {
    setResultTextView("Topside UP")
  }

  override fun onBottomSideUp() {
    setResultTextView("Bottomside UP")
  }

  override fun onRightSideUp() {
    setResultTextView("Rightside UP")
  }

  override fun onLeftSideUp() {
    setResultTextView("Leftside UP")
  }

  override fun onNear() {
    setResultTextView("Near")
  }

  override fun onFar() {
    setResultTextView("Far")
  }

  override fun onShakeDetected() {
    setResultTextView("Shake Detected!")
  }

  override fun onWave() {
    setResultTextView("Wave Detected!")
  }

  companion object {

    private val LOGTAG = "MainActivity"
    private val DEBUG = true
  }
}
