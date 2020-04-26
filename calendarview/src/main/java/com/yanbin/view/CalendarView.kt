package com.yanbin.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.TimeProvider

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
    private var currentAnimator: Animator? = null

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
                calendarRenderModel.scrollHorizontally(-distanceX)
                ViewCompat.postInvalidateOnAnimation(this@CalendarView)
                return true
            }
        }
        val gestureDetector = GestureDetector(context, gestureListener)
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val startOffset = calendarRenderModel.xOffset
                    val endOffset = calendarRenderModel.calculateSnapOffset()
                    if (!endOffset.isNaN()) {
                        startSnapAnimation(startOffset, endOffset)
                        ViewCompat.postInvalidateOnAnimation(this@CalendarView)
                    }
                }
            }

            gestureDetector.onTouchEvent(event)
        }
    }

    private fun startSnapAnimation(startOffset: Float, endOffset: Float) {
        val animator = ValueAnimator.ofFloat(startOffset, endOffset)
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator?.animatedValue as Float
            calendarRenderModel.xOffset = value
            ViewCompat.postInvalidateOnAnimation(this@CalendarView)
        }
        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                calendarRenderModel.onSnapComplete()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })

        animator.start()
        currentAnimator = animator
    }

    override fun onDraw(canvas: Canvas) {
        if (dayCellWidth == 0f) return

        drawDayCells(canvas)
    }

    private fun drawDayCells(canvas: Canvas) {
        val xScrollOffset = calendarRenderModel.xOffset
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
        calendarRenderModel.viewWidth = w.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = resolveSizeAndState(width, widthMeasureSpec, 1)
        val minHeight = WEEKS_OF_ONE_MONTH * dayCellHeight
        val newHeight = resolveSizeAndState(minHeight.toInt(), heightMeasureSpec, 1)
        setMeasuredDimension(newWidth, newHeight)
    }

    override fun onDetachedFromWindow() {
        currentAnimator?.cancel()
        super.onDetachedFromWindow()
    }


    companion object {
        private const val WEEKS_OF_ONE_MONTH = 5
    }

}
