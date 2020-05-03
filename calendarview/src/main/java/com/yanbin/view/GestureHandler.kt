package com.yanbin.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.ViewCompat

internal class GestureHandler(
    private val context: Context,
    private val renderModel: CalendarRenderModel,
    private val viewPort: CalendarViewPort
) {

    private var scrollMode = ScrollMode.IDLE

    @SuppressLint("ClickableViewAccessibility")
    fun bind(view: CalendarView) {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                if (scrollMode == ScrollMode.IDLE) {
                    if (distanceX != 0f) {
                        scrollMode = ScrollMode.HORIZONTAL
                    } else if(distanceY != 0f) {
                        scrollMode = ScrollMode.VERTICAL
                    }
                }

                if (scrollMode == ScrollMode.HORIZONTAL) {
                    viewPort.scrollHorizontally(-distanceX)
                    ViewCompat.postInvalidateOnAnimation(view)
                } else if (scrollMode == ScrollMode.VERTICAL) {
                    viewPort.resizeVertically(-distanceY)
                    view.requestLayout()
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
                    val startOffset = viewPort.xOffset
                    val endOffset = viewPort.calculateSnapOffset()
                    scrollMode = ScrollMode.IDLE

                    view.startSnapAnimation(startOffset, endOffset)
                    view.postInvalidate()
                }
            }

            gestureDetector.onTouchEvent(event)
        }
    }

    enum class ScrollMode {
        IDLE, HORIZONTAL, VERTICAL
    }
}