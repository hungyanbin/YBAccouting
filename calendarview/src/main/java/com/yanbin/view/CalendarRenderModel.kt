package com.yanbin.view

import com.soywiz.klock.*

internal class CalendarRenderModel {

    val viewPort = CalendarViewPort()

    var thisMonthCells: List<DayCell> = listOf()
    var nextMonthCells: List<DayCell> = listOf()
    var prevMonthCells: List<DayCell> = listOf()

    var daySelected: (Date) -> Unit = {}

    private var currentDate = TimeProvider.now().date
    private var lastHighlightDay: DayCell? = null

    init {
        viewPort.setViewPortStateListener { state ->
            when(state) {
                ViewPortState.PREV_VIEW -> {
                    val dateOfPrevMonth = currentDate
                        .plus(DateTimeSpan(months = -1, days = -(currentDate.day - 1)))
                    setDate(dateOfPrevMonth)
                }
                ViewPortState.NEXT_VIEW -> {
                    val dateOfNextMonth = currentDate
                        .plus(DateTimeSpan(months = 1, days = -(currentDate.day - 1)))
                    setDate(dateOfNextMonth)
                }
            }
        }
    }

    fun setDate(date: Date) {
        currentDate = date
        thisMonthCells = DayTimeUtils.generateDayCellForThisMonth(date)
        val dateOfNextMonth = date.plus(MonthSpan(1))
        nextMonthCells = DayTimeUtils.generateDayCellForThisMonth(dateOfNextMonth)
        val dateOfPrevMonth = date.plus(MonthSpan(-1))
        prevMonthCells = DayTimeUtils.generateDayCellForThisMonth(dateOfPrevMonth)

        val firstDayOfThisMonth = thisMonthCells[currentDate.day - 1]
        firstDayOfThisMonth.selected = true
        daySelected.invoke(Date(currentDate.year, currentDate.month, firstDayOfThisMonth.dayOfMonth))
        lastHighlightDay = firstDayOfThisMonth
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
