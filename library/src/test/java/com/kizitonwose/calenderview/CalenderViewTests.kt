package com.kizitonwose.calenderview

import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.model.MonthConfig
import com.kizitonwose.calendarview.model.OutDateStyle
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.YearMonth

/**
 * These are core functionality tests.
 * The UI behaviour tests are in the sample project.
 */
class CalenderViewTests {

    // You can see what May and November 2020 with Monday as the first day of
    // week look like in the included May2020.png and November2020.png files.
    private val may2020 = YearMonth.of(2020, 5)
    private val nov2020 = may2020.plusMonths(6)
    private val firstDayOfWeek = DayOfWeek.MONDAY

    @Test
    fun `test all month in date generation works as expected`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, true, OutDateStyle.END_OF_ROW)

        val validInDateIndices = (0..1)
        val inDatesInMonth = weekDays.flatten().filterIndexed { index, _ -> validInDateIndices.contains(index) }

        // inDates are in appropriate indices and have accurate count.
        assertTrue(inDatesInMonth.all { it.owner == DayOwner.PREVIOUS_MONTH })
    }

    @Test
    fun `test no in date generation works as expected`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, false, OutDateStyle.END_OF_ROW)
        assertTrue(weekDays.flatten().none { it.owner == DayOwner.PREVIOUS_MONTH })
    }

    @Test
    fun `test first month in date generation works as expected`() {
        val months = MonthConfig.generateBoundedMonths(
            may2020, nov2020, firstDayOfWeek, 6, InDateStyle.FIRST_MONTH, OutDateStyle.NONE
        )

        // inDates are in the first month.
        assertTrue(months.first().weekDays.flatten().any { it.owner == DayOwner.PREVIOUS_MONTH })

        // No inDates in other months.
        assertTrue(months.takeLast(months.size - 1).all {
            it.weekDays.flatten().none { it.owner == DayOwner.PREVIOUS_MONTH }
        })
    }

    @Test
    fun `test end of row out date generation works as expected`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, true, OutDateStyle.END_OF_ROW)

        val validOutDateIndices = weekDays.flatten().indices.toList().takeLast(2)
        val outDatesInMonth = weekDays.flatten().filterIndexed { index, _ -> validOutDateIndices.contains(index) }

        // outDates are in appropriate indices and have accurate count.
        assertTrue(outDatesInMonth.all { it.owner == DayOwner.NEXT_MONTH })
    }

    @Test
    fun `test end of grid out date generation works as expected`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, true, OutDateStyle.END_OF_GRID)

        val validOutDateIndices = weekDays.flatten().indices.toList().takeLast(9)
        val outDatesInMonth = weekDays.flatten().filterIndexed { index, _ -> validOutDateIndices.contains(index) }

        // outDates are in appropriate indices and have accurate count.
        assertTrue(outDatesInMonth.all { it.owner == DayOwner.NEXT_MONTH })
    }

    @Test
    fun `test no out date generation works as expected`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, true, OutDateStyle.NONE)
        assertTrue(weekDays.flatten().none { it.owner == DayOwner.NEXT_MONTH })
    }

    @Test
    fun `test first day of week is in correct position`() {
        val weekDays = MonthConfig.generateWeekDays(may2020, firstDayOfWeek, true, OutDateStyle.END_OF_GRID)

        assertTrue(weekDays.first().first().date.dayOfWeek == firstDayOfWeek)
    }

    @Test
    fun `test max row count works with boundaries`() {
        val maxRowCount = 3
        val months = MonthConfig.generateBoundedMonths(
            may2020, may2020.plusMonths(20),
            firstDayOfWeek, maxRowCount, InDateStyle.ALL_MONTHS, OutDateStyle.END_OF_ROW
        )

        assertTrue(months.all { it.weekDays.count() <= maxRowCount })
        val mayCalendarMonths = months.filter { it.yearMonth == may2020 }
        assertTrue(mayCalendarMonths.count() == 2)

        assertTrue(mayCalendarMonths.first().weekDays.count() == 3)
        assertTrue(mayCalendarMonths.last().weekDays.count() == 2)

        assertTrue(mayCalendarMonths.first().indexInSameMonth == 0)
        assertTrue(mayCalendarMonths.last().indexInSameMonth == 1)

        assertTrue(mayCalendarMonths.first().numberOfSameMonth == 2)
        assertTrue(mayCalendarMonths.last().numberOfSameMonth == 2)
    }

    @Test
    fun `test max row count works without boundaries`() {
        val maxRowCount = 3
        val months = MonthConfig.generateUnboundedMonths(
            may2020.minusMonths(40), may2020.plusMonths(50),
            firstDayOfWeek, maxRowCount, InDateStyle.ALL_MONTHS, OutDateStyle.END_OF_GRID
        )
        assertTrue(months.all { it.weekDays.count() == maxRowCount })
    }

    @Test
    fun `test unbounded month generation does not exceed number of days in each month`() {
        val maxRowCount = 6
        MonthConfig.generateUnboundedMonths(
            YearMonth.of(2020, 2), YearMonth.of(2021, 2),
            DayOfWeek.SUNDAY, maxRowCount, InDateStyle.ALL_MONTHS, OutDateStyle.END_OF_GRID
        )
    }
}
