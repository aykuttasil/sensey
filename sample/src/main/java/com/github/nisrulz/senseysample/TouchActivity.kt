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

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.CompoundButton
import android.widget.TextView
import com.github.nisrulz.sensey.PinchScaleDetector
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TouchTypeDetector

class TouchActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

  private var txtResult: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_touch)

    // Init Sensey
    Sensey.instance.init(this)

    txtResult = findViewById(R.id.textView_result) as TextView

    val swt6 = findViewById(R.id.Switch6) as SwitchCompat
    swt6.setOnCheckedChangeListener(this)
    swt6.isChecked = false

    val swt7 = findViewById(R.id.Switch7) as SwitchCompat
    swt7.setOnCheckedChangeListener(this)
    swt7.isChecked = false
  }

  override fun onCheckedChanged(switchbtn: CompoundButton, isChecked: Boolean) {
    when (switchbtn.id) {
      R.id.Switch6 -> if (isChecked) {
        startTouchTypeDetection()
      } else {
        Sensey.instance.stopTouchTypeDetection()
      }
      R.id.Switch7 -> if (isChecked) {
        startPinchDetection()
      } else {
        Sensey.instance.stopPinchScaleDetection()
      }

      else -> {
      }
    }// Do nothing
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    // Setup onTouchEvent for detecting type of touch gesture
    Sensey.instance.setupDispatchTouchEvent(event)
    return super.dispatchTouchEvent(event)
  }

  override fun onPause() {
    super.onPause()

    Sensey.instance.stopTouchTypeDetection()
    Sensey.instance.stopPinchScaleDetection()
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

  private fun startPinchDetection() {
    Sensey.instance.startPinchScaleDetection(object : PinchScaleDetector.PinchScaleListener {
      override fun onScale(scaleGestureDetector: ScaleGestureDetector, isScalingOut: Boolean) {
        if (isScalingOut) {
          setResultTextView("Scaling Out")
        } else {
          setResultTextView("Scaling In")
        }
      }

      override fun onScaleStart(scaleGestureDetector: ScaleGestureDetector) {
        setResultTextView("Scaling : Started")
      }

      override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
        setResultTextView("Scaling : Stopped")
      }
    })
  }

  private fun startTouchTypeDetection() {
    Sensey.instance.startTouchTypeDetection(object : TouchTypeDetector.TouchTypListener {
      override fun onTwoFingerSingleTap() {
        setResultTextView("Two Finger Tap")
      }

      override fun onThreeFingerSingleTap() {
        setResultTextView("Three Finger Tap")
      }

      override fun onDoubleTap() {
        setResultTextView("Double Tap")
      }

      override fun onScroll(scrollDirection: Int) {
        when (scrollDirection) {
          TouchTypeDetector.SCROLL_DIR_UP -> setResultTextView("Scrolling Up")
          TouchTypeDetector.SCROLL_DIR_DOWN -> setResultTextView("Scrolling Down")
          TouchTypeDetector.SCROLL_DIR_LEFT -> setResultTextView("Scrolling Left")
          TouchTypeDetector.SCROLL_DIR_RIGHT -> setResultTextView("Scrolling Right")
          else -> {
          }
        }// Do nothing
      }

      override fun onSingleTap() {
        setResultTextView("Single Tap")
      }

      override fun onSwipe(swipeDirection: Int) {
        when (swipeDirection) {
          TouchTypeDetector.SWIPE_DIR_UP -> setResultTextView("Swipe Up")
          TouchTypeDetector.SWIPE_DIR_DOWN -> setResultTextView("Swipe Down")
          TouchTypeDetector.SWIPE_DIR_LEFT -> setResultTextView("Swipe Left")
          TouchTypeDetector.SWIPE_DIR_RIGHT -> setResultTextView("Swipe Right")
          else -> {
          }
        }//do nothing
      }

      override fun onLongPress() {
        setResultTextView("Long press")
      }
    })
  }

  companion object {

    private val LOGTAG = "TouchActivity"
    private val DEBUG = true
  }
}
