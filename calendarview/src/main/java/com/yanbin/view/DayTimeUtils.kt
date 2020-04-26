package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.TimeProvider

class DayTimeUtils {

    companion object {
        fun generateDayCellForThisMonth(today: Date): List<DayCell> {
            //get first day of the month
            val firstDayOfMonth = Date(today.year, today.month, 1)
            val dayNumberOfMonth = today.month.days(today.year)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeekInt

            val dayCells = mutableListOf<DayCell>()
            for (i in 0 until dayNumberOfMonth) {
                val dayOfWeekInt = firstDayOfWeek + i
                val dayNumber = firstDayOfMonth.day + i
                dayCells.add(DayCell(DayOfWeek[dayOfWeekInt], dayNumber))
            }

            return dayCells
        }
    }
}