package com.nakkeez.frametempmonitor.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.TextView

class OverlayService : Service(), View.OnTouchListener {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private var initialX: Int = 0
    private var initialY: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Create a new view and set its layout parameters
        overlayView = TextView(this).apply {
            text = "This is an overlay"
            textSize = 24f
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.LTGRAY)
            setBackgroundColor(Color.parseColor("#D9D3D3D3")) // (maybe E6 tai CC?) set a semi-transparent light grey color
            setOnTouchListener(this@OverlayService) // Set the touch listener
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        // Get the window manager and add the view to the window
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        Log.d("OverlayService", "View added to window")
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove the view from the window
        windowManager.removeView(overlayView)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Save the initial touch point
                initialX = event.rawX.toInt()
                initialY = event.rawY.toInt()

                // Set the view's alpha to indicate that it is being touched
                view.alpha = 0.6f
                return true
            }
            MotionEvent.ACTION_UP -> {
                // Check if the touch event was a click
                if (event.eventTime - event.downTime < ViewConfiguration.getTapTimeout()) {
                    view.performClick()
                }

                // Reset the view's alpha to indicate that it is no longer being touched
                view.alpha = 1.0f
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // Calculate the new position of the view
                val dx = event.rawX - initialX
                val dy = event.rawY - initialY

                // Update the view's layout parameters with the new position
                val layoutParams = view.layoutParams as WindowManager.LayoutParams
                layoutParams.x += dx.toInt()
                layoutParams.y += dy.toInt()
                windowManager.updateViewLayout(view, layoutParams)

                // Update the initial touch point
                initialX = event.rawX.toInt()
                initialY = event.rawY.toInt()
                return true
            }
        }

        return false
    }
}
