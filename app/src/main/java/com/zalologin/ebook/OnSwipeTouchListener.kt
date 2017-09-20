package com.zalologin.ebook

import android.content.Context
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.GestureDetector
import android.view.View


/**
 * OnSwipeTouchListener
 *
 * Created by HOME on 9/19/2017.
 */
open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {
    private var gestureDetector: GestureDetector
    private val MAX_CLICK_DURATION = 1000
    private val MAX_CLICK_DISTANCE = 15
    private var mcontext: Context = context
    private var pressStartTime: Long = 0
    private var pressedX: Float = 0.toFloat()
    private var pressedY: Float = 0.toFloat()
    private var stayedWithinClickDistance: Boolean = false
    private var locationx: Int = 0
    private var locationy: Int = 0

    private var inflationView: View? = null

    fun getLocationx(): Int = locationx

    fun setLocationx(locationx: Int) {
        this.locationx = locationx
    }

    fun getLocationy(): Int = locationy

    fun setLocationy(locationy: Int) {
        this.locationy = locationy
    }

    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    open fun newTouch() {}

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        this.inflationView = v
        gestureDetector.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_MOVE) {
            if (stayedWithinClickDistance && distance(pressedX, pressedY, event.x, event.y) > MAX_CLICK_DISTANCE) {
                stayedWithinClickDistance = false
            }
            return true
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            pressStartTime = System.currentTimeMillis()
            pressedX = event.x
            pressedY = event.y
            stayedWithinClickDistance = true

            return v.onTouchEvent(event)
        } else if (event.action == MotionEvent.ACTION_UP) {

            val pressDuration = System.currentTimeMillis() - pressStartTime
            if (pressDuration < MAX_CLICK_DURATION && stayedWithinClickDistance) {
                newTouch()
            }
            setLocationx(event.x.toInt())
            setLocationy(event.y.toInt())
            return v.onTouchEvent(event)
        } else {
            return v.onTouchEvent(event)
        }

    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        val distanceInPx = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        return pxToDp(distanceInPx)
    }

    private fun pxToDp(px: Float): Float = px / mcontext.getResources().getDisplayMetrics().density


    private inner class GestureListener : SimpleOnGestureListener() {
        val SWIPE_DISTANCE_THRESHOLD = 100
        val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean = true

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) {
                    System.out.println("ttt right")
                    onSwipeRight()
                }
                else {
                    System.out.println("ttt left")
                    onSwipeLeft()
                }
                return true
            }
            return false
        }
    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}