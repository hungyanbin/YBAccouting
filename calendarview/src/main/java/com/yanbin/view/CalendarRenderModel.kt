package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.MonthSpan
import com.soywiz.klock.plus

class CalendarRenderModel {

    var thisMonth: List<DayCell> = listOf()
    var nextMonth: List<DayCell> = listOf()
    var prevMonth: List<DayCell> = listOf()

    fun setDate(date: Date) {
        thisMonth = DayTimeUtils.generateDayCellForThisMonth(date)
        val dateOfNextMonth = date.plus(MonthSpan(1))
        nextMonth = DayTimeUtils.generateDayCellForThisMonth(dateOfNextMonth)
        val dateOfPrevMonth = date.plus(MonthSpan(-1))
        prevMonth = DayTimeUtils.generateDayCellForThisMonth(dateOfPrevMonth)
    }
}