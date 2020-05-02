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
                DayCell(DayOfWeek.Wednesday, 0, 1),
                DayCell(DayOfWeek.Thursday, 0, 2),
                DayCell(DayOfWeek.Friday, 0, 3),
                DayCell(DayOfWeek.Saturday, 0, 4),
                DayCell(DayOfWeek.Sunday, 1, 5),
                DayCell(DayOfWeek.Monday, 1, 6),
                DayCell(DayOfWeek.Tuesday, 1, 7),
                DayCell(DayOfWeek.Wednesday, 1, 8),
                DayCell(DayOfWeek.Thursday, 1, 9),
                DayCell(DayOfWeek.Friday, 1, 10),
                DayCell(DayOfWeek.Saturday, 1, 11),
                DayCell(DayOfWeek.Sunday, 2, 12),
                DayCell(DayOfWeek.Monday, 2, 13),
                DayCell(DayOfWeek.Tuesday, 2, 14),
                DayCell(DayOfWeek.Wednesday, 2, 15),
                DayCell(DayOfWeek.Thursday, 2, 16),
                DayCell(DayOfWeek.Friday, 2, 17),
                DayCell(DayOfWeek.Saturday, 2, 18),
                DayCell(DayOfWeek.Sunday, 3, 19),
                DayCell(DayOfWeek.Monday, 3 ,20),
                DayCell(DayOfWeek.Tuesday, 3 ,21),
                DayCell(DayOfWeek.Wednesday, 3 ,22),
                DayCell(DayOfWeek.Thursday, 3 ,23),
                DayCell(DayOfWeek.Friday, 3, 24),
                DayCell(DayOfWeek.Saturday, 3 ,25),
                DayCell(DayOfWeek.Sunday, 4 ,26),
                DayCell(DayOfWeek.Monday, 4, 27),
                DayCell(DayOfWeek.Tuesday, 4, 28),
                DayCell(DayOfWeek.Wednesday, 4, 29),
                DayCell(DayOfWeek.Thursday, 4, 30)
            ))
    }
}