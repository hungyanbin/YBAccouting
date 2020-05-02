package com.yanbin.view

import com.soywiz.klock.*

internal class CalendarRenderModel {

    var thisMonthCells: List<DayCell> = listOf()
    var nextMonthCells: List<DayCell> = listOf()
    var prevMonthCells: List<DayCell> = listOf()

    var xOffset = 0f
    var viewWidth = 0f
    var daySelected: (Date) -> Unit = {}

    private var currentDate = TimeProvider.now().date
    private var lastHighlightDay: DayCell? = null
    private var state = CalendarViewState.IDLE

    fun setDate(date: Date) {
        currentDate = date
        thisMonthCells = DayTimeUtils.generateDayCellForThisMonth(date)
        val dateOfNextMonth = date.plus(MonthSpan(1))
        nextMonthCells = DayTimeUtils.generateDayCellForThisMonth(dateOfNextMonth)
        val dateOfPrevMonth = date.plus(MonthSpan(-1))
        prevMonthCells = DayTimeUtils.generateDayCellForThisMonth(dateOfPrevMonth)

        thisMonthCells[currentDate.day - 1].selected = true
        lastHighlightDay = thisMonthCells[currentDate.day - 1]
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
            val dateOfPrevMonth = currentDate
                .plus(DateTimeSpan(months = -1, days = -(currentDate.day - 1)))
            setDate(dateOfPrevMonth)
        } else if (xOffset == -viewWidth) {
            val dateOfNextMonth = currentDate
                .plus(DateTimeSpan(months = 1, days = -(currentDate.day - 1)))
            setDate(dateOfNextMonth)
        }

        xOffset = 0f
        state = CalendarViewState.IDLE
    }

    fun onCellTouched(row: Int, column: Int) {
        val selectedCell = thisMonthCells.find { it.weekDay.index0 == column && it.weekOfMonth == row }
        selectedCell?.let { dayCell ->
            lastHighlightDay?.selected = false
            dayCell.selected = true
            lastHighlightDay = dayCell
            daySelected.invoke(Date(currentDate.year, currentDate.month, dayCell.dayOfMonth))
        }
    }
}

internal enum class CalendarViewState {
    IDLE, SCROLLING, SNAP
}