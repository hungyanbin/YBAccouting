package com.yanbin.view

import com.soywiz.klock.DayOfWeek

data class DayCell(val weekDay: DayOfWeek, val dayOfMonth: Int, var selected: Boolean = false)