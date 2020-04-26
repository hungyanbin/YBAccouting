package com.yanbin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
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
    private var currentDayCell = listOf<DayCell>()

    private fun init(context: Context) {
        with(dayTextPaint) {
            color = Color.BLACK
            textSize = 14.dp().toFloat()
            textAlign = Paint.Align.CENTER
        }

        val fontMetrics = dayTextPaint.fontMetrics
        dayCellDrawOffset = -fontMetrics.top
        dayCellHeight = fontMetrics.bottom - fontMetrics.top + 8.dp()

        currentDayCell = DayTimeUtils.generateDayCellForThisMonth(TimeProvider)
    }

    override fun onDraw(canvas: Canvas) {
        if (dayCellWidth == 0f) return

        var rowNumber = 0
        currentDayCell.forEach { dayCell ->
            val textCenterX = (dayCell.weekDay.index0 + 0.5f)* dayCellWidth
            val textCenterY = dayCellDrawOffset + rowNumber * dayCellHeight
            canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textCenterY, dayTextPaint)
            if (dayCell.weekDay == DayOfWeek.Saturday) {
                rowNumber ++
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayCellWidth = w / 7f
    }



    //display current month
    //first column of the day is Sunday

}
