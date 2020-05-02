package com.yanbin.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import com.soywiz.klock.Date

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
    private val dayHighlightTextPaint = Paint()
    private val dayHighlightPaint = Paint()
    private val calendarRenderModel = CalendarRenderModel(CalendarViewPort())
    private var currentAnimator: Animator? = null

    private fun init(context: Context) {
        with(dayTextPaint) {
            color = Color.BLACK
            textSize = 14.dp().toFloat()
            textAlign = Paint.Align.CENTER
        }

        with(dayHighlightTextPaint) {
            color = Color.WHITE
            textSize = 14.dp().toFloat()
            textAlign = Paint.Align.CENTER
        }

        with(dayHighlightPaint) {
            val outValue = TypedValue()
            val colorAttr = android.R.attr.colorAccent
            context.theme.resolveAttribute(colorAttr, outValue, true)
            color = outValue.data
        }

        val fontMetrics = dayTextPaint.fontMetrics
        dayCellDrawOffset = -fontMetrics.top + 4.dp()
        dayCellHeight = fontMetrics.bottom - fontMetrics.top + 8.dp()

        setupGesture(context)

        isClickable = true
        isFocusable = true
    }

    private fun setupGesture(context: Context) {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                calendarRenderModel.viewPort.scrollHorizontally(-distanceX)
                ViewCompat.postInvalidateOnAnimation(this@CalendarView)
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val touchedColumn = (e.x / dayCellWidth).toInt()
                val touchedRow = (e.y / dayCellHeight).toInt()
                calendarRenderModel.onCellTouched(touchedRow, touchedColumn)
                postInvalidate()
                return true
            }
        }
        val gestureDetector = GestureDetector(context, gestureListener)
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val startOffset = calendarRenderModel.viewPort.xOffset
                    val endOffset = calendarRenderModel.viewPort.calculateSnapOffset()
                    if (!endOffset.isNaN()) {
                        startSnapAnimation(startOffset, endOffset)
                        postInvalidate()
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
            calendarRenderModel.viewPort.xOffset = value
            ViewCompat.postInvalidateOnAnimation(this@CalendarView)
        }
        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                calendarRenderModel.viewPort.onSnapComplete()
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
        val xScrollOffset = calendarRenderModel.viewPort.xOffset
        drawDayCells(canvas, calendarRenderModel.thisMonthCells, xScrollOffset)
        drawDayCells(canvas, calendarRenderModel.nextMonthCells, xScrollOffset + width)
        drawDayCells(canvas, calendarRenderModel.prevMonthCells, xScrollOffset - width)
    }

    private fun drawDayCells(canvas: Canvas, dayCells: List<DayCell>, xOffset: Float) {
        dayCells.forEach { dayCell ->
            val textCenterX = (dayCell.weekDay.index0 + 0.5f) * dayCellWidth + xOffset
            val textBaseY = dayCellDrawOffset + dayCell.weekOfMonth * dayCellHeight
            val textCenterY = dayCellHeight/2 + dayCell.weekOfMonth * dayCellHeight
            if (dayCell.selected){
                canvas.drawCircle(textCenterX, textCenterY, dayCellHeight/2, dayHighlightPaint)
                canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textBaseY, dayHighlightTextPaint)
            } else {
                canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textBaseY, dayTextPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayCellWidth = w / 7f
        calendarRenderModel.viewPort.viewWidth = w.toFloat()
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

    fun toDate(date: Date) {
        calendarRenderModel.setDate(date)
    }

    fun setDaySelectedListener(onDaySelected: (Date) -> Unit) {
        calendarRenderModel.daySelected = onDaySelected
    }

    companion object {
        private const val WEEKS_OF_ONE_MONTH = 5
    }

}
