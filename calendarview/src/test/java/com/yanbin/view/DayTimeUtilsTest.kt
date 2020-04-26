package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.DayOfWeek
import org.assertj.core.api.Assertions
import org.junit.Test

class DayTimeUtilsTest {

    @Test
    fun `get DayCellForThisMonth`() {
        val date = Date(2020, 4, 23)
        val dayCells = DayTimeUtils.generateDayCellForThisMonth(date)

        Assertions.assertThat(dayCells)
            .isEqualTo(listOf(
                DayCell(DayOfWeek.Wednesday, 1),
                DayCell(DayOfWeek.Thursday, 2),
                DayCell(DayOfWeek.Friday, 3),
                DayCell(DayOfWeek.Saturday, 4),
                DayCell(DayOfWeek.Sunday, 5),
                DayCell(DayOfWeek.Monday, 6),
                DayCell(DayOfWeek.Tuesday, 7),
                DayCell(DayOfWeek.Wednesday, 8),
                DayCell(DayOfWeek.Thursday, 9),
                DayCell(DayOfWeek.Friday, 10),
                DayCell(DayOfWeek.Saturday, 11),
                DayCell(DayOfWeek.Sunday, 12),
                DayCell(DayOfWeek.Monday, 13),
                DayCell(DayOfWeek.Tuesday, 14),
                DayCell(DayOfWeek.Wednesday, 15),
                DayCell(DayOfWeek.Thursday, 16),
                DayCell(DayOfWeek.Friday, 17),
                DayCell(DayOfWeek.Saturday, 18),
                DayCell(DayOfWeek.Sunday, 19),
                DayCell(DayOfWeek.Monday, 20),
                DayCell(DayOfWeek.Tuesday, 21),
                DayCell(DayOfWeek.Wednesday, 22),
                DayCell(DayOfWeek.Thursday, 23),
                DayCell(DayOfWeek.Friday, 24),
                DayCell(DayOfWeek.Saturday, 25),
                DayCell(DayOfWeek.Sunday, 26),
                DayCell(DayOfWeek.Monday, 27),
                DayCell(DayOfWeek.Tuesday, 28),
                DayCell(DayOfWeek.Wednesday, 29),
                DayCell(DayOfWeek.Thursday, 30)
            ))
    }
}