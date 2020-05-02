package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.MonthSpan
import com.soywiz.klock.TimeProvider
import com.soywiz.klock.plus

internal class CalendarRenderModel {

    var thisMonth: List<DayCell> = listOf()
    var nextMonth: List<DayCell> = listOf()
    var prevMonth: List<DayCell> = listOf()

    var xOffset = 0f
    var state = CalendarViewState.IDLE
    var viewWidth = 0f

    private var currentDate = TimeProvider.now().date
    private var lastHighlightDay: DayCell? = null

    fun setDate(date: Date) {
        currentDate = date
        thisMonth = DayTimeUtils.generateDayCellForThisMonth(date)
        val dateOfNextMonth = date.plus(MonthSpan(1))
        nextMonth = DayTimeUtils.generateDayCellForThisMonth(dateOfNextMonth)
        val dateOfPrevMonth = date.plus(MonthSpan(-1))
        prevMonth = DayTimeUtils.generateDayCellForThisMonth(dateOfPrevMonth)

        thisMonth[0].selected = true
        lastHighlightDay = thisMonth[0]
    }

    fun scrollHorizontally(distance: Float) {
        if (state == CalendarViewState.SNAP) {
            return
        }

        xOffset += distance
        state = CalendarViewState.SCROLLING
    }

    fun calculateSnapOffset(): Float {
        return if (state == CalendarViewState.SCROLLING) {
            state = CalendarViewState.SNAP

            if (xOffset in 0f..viewWidth / 2 || xOffset in -viewWidth / 2..0f) {
                0f
            } else if (xOffset > viewWidth / 2) {
                viewWidth
            } else {
                -viewWidth
            }
        } else {
            Float.NaN
        }
    }

    fun onSnapComplete() {
        if (xOffset == viewWidth) {
            val dateOfPrevMonth = currentDate.plus(MonthSpan(-1))
            setDate(dateOfPrevMonth)
        } else if (xOffset == -viewWidth) {
            val dateOfNextMonth = currentDate.plus(MonthSpan(1))
            setDate(dateOfNextMonth)
        }

        xOffset = 0f
        state = CalendarViewState.IDLE
    }

    fun onCellTouched(row: Int, column: Int) {
        val selectedCell = thisMonth.find { it.weekDay.index0 == column && it.weekOfMonth == row }
        selectedCell?.let { dayCell ->
            lastHighlightDay?.selected = false
            dayCell.selected = true
            lastHighlightDay = dayCell
        }
    }
}

internal enum class CalendarViewState {
    IDLE, SCROLLING, SNAP
}