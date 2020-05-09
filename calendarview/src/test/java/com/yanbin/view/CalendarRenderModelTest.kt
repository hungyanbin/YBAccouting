package com.yanbin.view

import com.soywiz.klock.Date
import com.soywiz.klock.DayOfWeek
import com.yanbin.view.animation.AnimationPlayer
import com.yanbin.view.animation.CalendarAnimationFactory
import com.yanbin.view.animation.SnapAnimation
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class CalendarRenderModelTest {

    private lateinit var calendarRenderModel: CalendarRenderModel
    private lateinit var viewPort: CalendarViewPort

    @Before
    fun setUp() {
        val calendarAnimationFactory = object: CalendarAnimationFactory {
            override fun empty(): SnapAnimation {
                return FakeSnapAnimation()
            }

            override fun horizontal(startOffset: Float, endOffset: Float): SnapAnimation {
                return FakeSnapAnimation()
            }

            override fun vertical(startOffset: Float, endOffset: Float, startHeight: Float, endHeight: Float): SnapAnimation {
                return FakeSnapAnimation()
            }
        }
        viewPort = CalendarViewPort(calendarAnimationFactory)
        calendarRenderModel = CalendarRenderModel(viewPort)
    }

    @Test
    fun `setDate on 2020-04-03, prev month should have 31 days and next month should have 31 days`() {
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
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-01-01 is Wednesday
                monthCells.size == 31 && monthCells[0].weekDay == DayOfWeek.Wednesday
            }
    }

    @Test
    fun `Scroll to right with less than half of width should snap to this month`() {
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(400f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-02-01 is Saturday
                monthCells.size == 29 && monthCells[0].weekDay == DayOfWeek.Saturday
            }
    }

    @Test
    fun `Scroll to left with more than half of width should snap to next month`() {
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(-600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-03-01 is Saturday
                monthCells.size == 31 && monthCells[0].weekDay == DayOfWeek.Sunday
            }
    }

    @Test
    fun `Scroll to left with less than half of width should snap to this month`() {
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f

        //act
        scrollAndSnap(-300f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                //2020-02-01 is Saturday
                monthCells.size == 29 && monthCells[0].weekDay == DayOfWeek.Saturday
            }
    }

    @Test
    fun `2020-02-12 scroll to next month should select 2020-03-01`() {
        var selectDate: Date? = null
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f
        calendarRenderModel.daySelected = { selectDate = it}

        //act
        scrollAndSnap(-600f)

        //assert
        //this month becomes 2020-03
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[0].dayOfMonth == 1 && monthCells[0].selected
            }
        Assertions.assertThat(selectDate)
            .isEqualTo(Date(2020, 3, 1))
    }

    @Test
    fun `2020-02-12 scroll to prev month should select 2020-01-01`() {
        var selectDate: Date? = null
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f
        calendarRenderModel.daySelected = { selectDate = it}

        //act
        scrollAndSnap(600f)

        //assert
        //this month becomes 2020-01
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { monthCells ->
                monthCells[0].dayOfMonth == 1 && monthCells[0].selected
            }
        Assertions.assertThat(selectDate)
            .isEqualTo(Date(2020, 1, 1))
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
        calendarRenderModel.setDate(Date(2020, 2, 12))
        viewPort.viewWidth = 1000f

        //act
        viewPort.scrollHorizontally(600f)
        viewPort.snapHorizontally(700f)
        Assertions.assertThat(viewPort.xOffset).isEqualTo(700f)
        viewPort.scrollHorizontally(333f)

        //assert
        Assertions.assertThat(viewPort.xOffset).isEqualTo(700f)
    }

    @Test
    fun `update badge on 2020-04-23, 2020-05-01 should be able see badge on these days`() {
        calendarRenderModel.setDate(Date(2020, 4, 12))

        //act
        calendarRenderModel.updateBadges(listOf(
            Date(2020, 4, 23),
            Date(2020, 5, 1)
        ))

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { cells ->
                cells[23 - 1].hasBadge
            }
        Assertions.assertThat(calendarRenderModel.nextMonthCells)
            .matches { cells ->
                cells[1 - 1].hasBadge
            }
    }

    @Test
    fun `update badge on 2020-05-01 and scroll to next month should be able to see badge`() {
        calendarRenderModel.setDate(Date(2020, 4, 12))
        viewPort.viewWidth = 1000f

        //act
        calendarRenderModel.updateBadges(listOf(
            Date(2020, 5, 1)
        ))
        scrollAndSnap(-600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { cells ->
                cells[1 - 1].hasBadge
            }
    }

    @Test
    fun `update badge on 2020-03-12 and scroll to prev month should be able to see badge`() {
        calendarRenderModel.setDate(Date(2020, 4, 12))
        viewPort.viewWidth = 1000f

        //act
        calendarRenderModel.updateBadges(listOf(
            Date(2020, 3, 12)
        ))
        scrollAndSnap(600f)

        //assert
        Assertions.assertThat(calendarRenderModel.thisMonthCells)
            .matches { cells ->
                cells[12 - 1].hasBadge
            }
    }

    private fun scrollAndSnap(distance: Float) {
        viewPort.scrollHorizontally(distance)
        val snapOffset = viewPort.calculateSnapOffset()
        viewPort.snapHorizontally(snapOffset)
        viewPort.onSnapComplete()
    }

    private class FakeSnapAnimation: SnapAnimation {
        override fun start(viewPort: CalendarViewPort): AnimationPlayer {
            return FakeAnimationPlayer()
        }
    }

    private class FakeAnimationPlayer: AnimationPlayer {
        override fun cancel() {}
    }
}