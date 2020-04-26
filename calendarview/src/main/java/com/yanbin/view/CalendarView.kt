package com.yanbin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CalendarView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private var dayCellWidth = 0f
    private var dayCellDrawOffset = 0f
    private val dayTextPaint = Paint()

    private fun init(context: Context) {
        with(dayTextPaint) {
            color = Color.BLACK
            textSize = 14.dp().toFloat()
            textAlign = Paint.Align.CENTER
        }

        val fontMetrics = dayTextPaint.fontMetrics
        dayCellDrawOffset = -fontMetrics.top
    }

    override fun onDraw(canvas: Canvas) {
        if (dayCellWidth == 0f) return

        for (i in 0..6) {
            val textCenterX = (i + 0.5f)* dayCellWidth
            val textCenterY = dayCellDrawOffset
            canvas.drawText(i.toString(), textCenterX, textCenterY, dayTextPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayCellWidth = w / 7f
    }
}