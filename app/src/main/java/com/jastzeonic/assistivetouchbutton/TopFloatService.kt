package com.jastzeonic.assistivetouchbutton

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.ImageView
import android.R.attr.y
import android.R.attr.x
import android.R.attr.gravity
import android.graphics.PixelFormat
import android.provider.Settings
import android.util.Log
import android.view.*
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION




class TopFloatService : Service() {

    lateinit var windowManager: WindowManager
    var ballWindowManagerParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
    lateinit var ballView: View
    lateinit var menuView: View
    lateinit var floatImage: ImageView
    var touchStartX: Float = 0f
    var touchStartY: Float = 0f
    var x: Float = 0f
    var y: Float = 0f
    var isMoving: Boolean = false;


    override fun onCreate() {
        super.onCreate()
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        startActivity(myIntent)

        ballView = LayoutInflater.from(this).inflate(R.layout.floatball, null, false)
        floatImage = ballView as ImageView


        ballWindowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        ballWindowManagerParams.gravity = Gravity.START or Gravity.TOP
        ballWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        ballWindowManagerParams.x = 0
        ballWindowManagerParams.y = 0
        ballWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        ballWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        ballWindowManagerParams.format = PixelFormat.RGBA_8888

        windowManager.addView(ballView, ballWindowManagerParams)
        floatImage.setOnTouchListener(onTouchListener)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    val onTouchListener: View.OnTouchListener = View.OnTouchListener { view, event ->
        x = event.rawX.toInt().toFloat()
        y = event.rawY.toInt().toFloat()

        Log.v("TopFloatService", "event.action:" + event.action)
        Log.v("TopFloatService", "event.X:" + event.x)
        Log.v("TopFloatService", "event.rawX:" + event.rawX)
        Log.v("TopFloatService", "event.Y:" + event.y)
        Log.v("TopFloatService", "event.rawY:" + event.rawY)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoving = false
                touchStartX = (event.x).toInt().toFloat()
                touchStartY = (event.y).toInt().toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                isMoving = true
                updateViewPosition()
            }
            MotionEvent.ACTION_UP -> {
                touchStartX = 0f
                touchStartY = 0f
            }

        }

        isMoving

    }

    fun updateViewPosition() {
        ballWindowManagerParams.x = (x - touchStartX).toInt()
        ballWindowManagerParams.y = (y - touchStartY).toInt()
        windowManager.updateViewLayout(ballView, ballWindowManagerParams)
    }
}
