package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.DayOfWeek
import org.assertj.core.api.Assertions
import org.junit.Test

class CalendarRenderModelTest {

    @Test
    fun `setDate on 2020-04-03, prev month should have 31 days and next month should have 31 days`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 4, 3))

        Assertions.assertThat(calendarRenderModel.prevMonthCells)
            .matches { monthCells ->
                monthCells.size == 31 && monthCells[0].dayOfMonth == 1
                    && monthCells[0].weekDay == DayOfWeek.Sunday
            }

        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells.size == 30 && monthCells[0].dayOfMonth == 1
                    && monthCells[0].weekDay == DayOfWeek.Wednesday
            }

        Assertions.assertThat(calendarRenderModel.nextMonthCells)
            .matches { monthCells ->
                monthCells.size == 31 && monthCells[0].dayOfMonth == 1
                    && monthCells[0].weekDay == DayOfWeek.Friday
            }
    }

    @Test
    fun `setDate on 2020-02-13, the day should be selected`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))

        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                !monthCells[13 - 1].selected
            }

        calendarRenderModel.setDate(Date(2020, 2, 13))

        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[13 - 1].selected
            }
    }

    @Test
    fun `Scroll to right with more than half of width should snap to prev month`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, 600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-01-01 is Wednesday
                monthCells.size == 31 && monthCells[0].weekDay == DayOfWeek.Wednesday
            }
    }

    @Test
    fun `Scroll to right with less than half of width should snap to this month`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, 400f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-02-01 is Saturday
                monthCells.size == 29 && monthCells[0].weekDay == DayOfWeek.Saturday
            }
    }

    @Test
    fun `Scroll to left with more than half of width should snap to next month`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, -600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-03-01 is Saturday
                monthCells.size == 31 && monthCells[0].weekDay == DayOfWeek.Sunday
            }
    }

    @Test
    fun `Scroll to left with less than half of width should snap to this month`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, -300f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-02-01 is Saturday
                monthCells.size == 29 && monthCells[0].weekDay == DayOfWeek.Saturday
            }
    }

    @Test
    fun `2020-02-12 scroll to next month should select 2020-03-01`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, -600f)

        //assert
        //this month becomes 2020-03
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[0].dayOfMonth == 1 && monthCells[0].selected
            }
    }

    @Test
    fun `2020-02-12 scroll to prev month should select 2020-01-01`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(calendarRenderModel, 600f)

        //assert
        //this month becomes 2020-03
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[0].dayOfMonth == 1 && monthCells[0].selected
            }
    }

    /*              2020-02
                          1
        2  3  4  5  6  7  8
        9 10 11 12 13 14 15
       16 17 18 19 20 21 22
       23 24 25 26 27 28 29
     */
    @Test
    fun `touch (4, 0) should select 02-23`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                !monthCells[23 - 1].selected
            }

        //act
        calendarRenderModel.onCellTouched(4, 0)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[23 - 1].selected
            }
    }

    @Test
    fun `not able to scroll if it is snapping`() {
        val calendarRenderModel = CalendarRenderModel()
        calendarRenderModel.setDate(Date(2020, 2, 12))
        calendarRenderModel.viewPort.viewWidth = 1000f

        //act
        calendarRenderModel.viewPort.scrollHorizontally(600f)
        calendarRenderModel.viewPort.snapHorizontally(700f)
        Assertions.assertThat(calendarRenderModel.viewPort.xOffset).isEqualTo(700f)
        calendarRenderModel.viewPort.scrollHorizontally(333f)

        //assert
        Assertions.assertThat(calendarRenderModel.viewPort.xOffset).isEqualTo(700f)
    }

    private fun scrollAndSnap(calendarRenderModel: CalendarRenderModel, distance: Float) {
        calendarRenderModel.viewPort.scrollHorizontally(distance)
        val snapOffset = calendarRenderModel.viewPort.calculateSnapOffset()
        calendarRenderModel.viewPort.snapHorizontally(snapOffset)
        calendarRenderModel.viewPort.onSnapComplete()
    }
}