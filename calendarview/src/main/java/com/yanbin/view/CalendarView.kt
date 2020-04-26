package com.yanbin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import com.soywiz.klock.*

class CalendarView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private var dayCellWidth = 0f
    private var dayCellDrawOffset = 0f
    private var dayCellHeight = 0f
    private val dayTextPaint = Paint()
    private val calendarRenderModel = CalendarRenderModel()
    private var xScrollOffset = 0f

    private fun init(context: Context) {
        with(dayTextPaint) {
            color = Color.BLACK
            textSize = 14.dp().toFloat()
            textAlign = Paint.Align.CENTER
        }

        val fontMetrics = dayTextPaint.fontMetrics
        dayCellDrawOffset = -fontMetrics.top
        dayCellHeight = fontMetrics.bottom - fontMetrics.top + 8.dp()

        val today = TimeProvider.now().date
        calendarRenderModel.setDate(today)

        setupGesture(context)

        isClickable = true
        isFocusable = true
    }

    private fun setupGesture(context: Context) {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                xScrollOffset -= distanceX
                ViewCompat.postInvalidateOnAnimation(this@CalendarView)
                return true
            }
        }
        val gestureDetector = GestureDetector(context, gestureListener)
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    xScrollOffset = 0f
                    ViewCompat.postInvalidateOnAnimation(this@CalendarView)
                }
            }

            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (dayCellWidth == 0f) return

        drawDayCells(canvas)
    }

    private fun drawDayCells(canvas: Canvas) {
        drawDayCells(canvas, calendarRenderModel.thisMonth, xScrollOffset)
        drawDayCells(canvas, calendarRenderModel.nextMonth, xScrollOffset + width)
        drawDayCells(canvas, calendarRenderModel.prevMonth, xScrollOffset - width)
    }

    private fun drawDayCells(canvas: Canvas, dayCells: List<DayCell>, xOffset: Float) {
        var rowNumber = 0
        dayCells.forEach { dayCell ->
            val textCenterX = (dayCell.weekDay.index0 + 0.5f) * dayCellWidth + xOffset
            val textCenterY = dayCellDrawOffset + rowNumber * dayCellHeight
            canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textCenterY, dayTextPaint)
            if (dayCell.weekDay == DayOfWeek.Saturday) {
                rowNumber++
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayCellWidth = w / 7f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = resolveSizeAndState(width, widthMeasureSpec, 1)
        val minHeight = WEEKS_OF_ONE_MONTH * dayCellHeight
        val newHeight = resolveSizeAndState(minHeight.toInt(), heightMeasureSpec, 1)
        setMeasuredDimension(newWidth, newHeight)
    }


    companion object {
        private const val WEEKS_OF_ONE_MONTH = 5
    }

}
