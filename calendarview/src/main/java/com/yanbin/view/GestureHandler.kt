package com.yanbin.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.ViewCompat
import kotlin.math.abs

internal class GestureHandler(
    private val context: Context,
    private val renderModel: CalendarRenderModel,
    private val viewPort: CalendarViewPort
) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(view: CalendarView) {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                if (viewPort.direction == Direction.NON) {
                    if (abs(distanceX) > abs(distanceY) && distanceX != 0f) {
                        viewPort.direction = Direction.HORIZONTAL
                    } else if(abs(distanceX) < abs(distanceY) && distanceY != 0f) {
                        viewPort.direction = Direction.VERTICAL
                    }
                }

                when(viewPort.direction) {
                    Direction.HORIZONTAL -> {
                        viewPort.scrollHorizontally(-distanceX)
                        ViewCompat.postInvalidateOnAnimation(view)
                    }
                    Direction.VERTICAL -> {
                        viewPort.resizeVertically(-distanceY)
                        view.requestLayout()
                    }
                }
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val touchedColumn = (e.x / view.dayCellWidth).toInt()
                val touchedRow = (e.y / view.dayCellHeight).toInt()
                renderModel.onCellTouched(touchedRow, touchedColumn)
                view.postInvalidate()
                return true
            }
        }
        val gestureDetector = GestureDetector(context, gestureListener)

        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (viewPort.state == ViewPortState.SCROLL) {
                        val animation = viewPort.generateSnapAnimation()
                        view.startSnapAnimation(animation)
                        viewPort.direction = Direction.NON
                    }
                }
            }

            gestureDetector.onTouchEvent(event)
        }
    }

}