package com.yanbin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.soywiz.klock.Date
import com.yanbin.view.animation.AnimationPlayer
import com.yanbin.view.animation.CalendarAnimationFactoryImp
import com.yanbin.view.animation.SnapAnimation

class CalendarView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    internal var dayCellWidth = 0f
        private set
    internal var dayCellHeight = 0f
        private set
    private var dayCellDrawOffset = 0f
    private var badgeYOffset = 0f
    private var dayTextCenterOffset = 0f
    private var selectedDayCellRadius = 0f
    private val dayTextPaint = Paint()
    private val dayHighlightTextPaint = Paint()
    private val dayHighlightPaint = Paint()
    private val badgePaint = Paint()
    private val dayCellPaddingTop = 6.dp()
    private val dayCellPaddingBottom = 12.dp()
    private val viewPort = CalendarViewPort(CalendarAnimationFactoryImp())
    private val calendarRenderModel = CalendarRenderModel(viewPort)
    private lateinit var gestureHandler: GestureHandler
    private var currentAnimator: AnimationPlayer? = null

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

        with(badgePaint) {
            val outValue = TypedValue()
            val colorAttr = android.R.attr.colorPrimary
            context.theme.resolveAttribute(colorAttr, outValue, true)
            color = outValue.data
        }

        val fontMetrics = dayTextPaint.fontMetrics
        dayCellDrawOffset = -fontMetrics.top + dayCellPaddingTop
        val textHeight = fontMetrics.bottom - fontMetrics.top
        dayCellHeight = textHeight + dayCellPaddingTop + dayCellPaddingBottom
        badgeYOffset = textHeight + dayCellPaddingTop + dayCellPaddingBottom/2
        dayTextCenterOffset = textHeight/2 + dayCellPaddingTop
        selectedDayCellRadius = textHeight/2 + 5.dp()
        viewPort.setDayCellHeight(dayCellHeight)
        viewPort.setViewRequestEventListenere { request ->
            when(request) {
                ViewRequest.DRAW -> postInvalidate()
                ViewRequest.LAYOUT -> requestLayout()
            }
        }

        gestureHandler = GestureHandler(context, calendarRenderModel, viewPort)
        gestureHandler.bind(this)

        isClickable = true
        isFocusable = true
    }

    internal fun startSnapAnimation(snapAnimation: SnapAnimation) {
        currentAnimator = snapAnimation.start(viewPort)
    }

    override fun onDraw(canvas: Canvas) {
        if (dayCellWidth == 0f) return

        drawDayCells(canvas)
    }

    private fun drawDayCells(canvas: Canvas) {
        val xScrollOffset = viewPort.xOffset
        drawDayCells(canvas, calendarRenderModel.thisMonthCells, xScrollOffset)
        drawDayCells(canvas, calendarRenderModel.nextMonthCells, xScrollOffset + width)
        drawDayCells(canvas, calendarRenderModel.prevMonthCells, xScrollOffset - width)
    }

    private fun drawDayCells(canvas: Canvas, dayCells: List<DayCell>, xOffset: Float) {
        dayCells.forEach { dayCell ->
            val textCenterX = (dayCell.weekDay.index0 + 0.5f) * dayCellWidth + xOffset
            val dayCellDrawTop = dayCell.weekOfMonth * dayCellHeight + viewPort.yOffset
            val textBaseY = dayCellDrawOffset + dayCellDrawTop
            val textCenterY = dayTextCenterOffset + dayCellDrawTop
            if (dayCell.selected) {
                canvas.drawCircle(textCenterX, textCenterY, selectedDayCellRadius, dayHighlightPaint)
                canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textBaseY, dayHighlightTextPaint)
            } else {
                if (dayCell.hasBadge) {
                    val badgeCenterY = badgeYOffset + dayCellDrawTop
                    canvas.drawCircle(textCenterX, badgeCenterY, 2.dp().toFloat(), badgePaint)
                }
                canvas.drawText(dayCell.dayOfMonth.toString(), textCenterX, textBaseY, dayTextPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayCellWidth = w / 7f
        viewPort.viewWidth = w.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = resolveSizeAndState(width, widthMeasureSpec, 1)
        val minHeight = viewPort.viewHeight
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

    fun updateBadges(badgeDates: List<Date>) {
        calendarRenderModel.updateBadges(badgeDates)
    }

}
