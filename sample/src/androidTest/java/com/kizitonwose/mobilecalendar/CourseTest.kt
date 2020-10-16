package com.kizitonwose.mobilecalendar

import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CourseTest {
    private lateinit var sample1 : Course
    private lateinit var sample2 : Course
    private lateinit var sample3 : Course
    @Before
    fun setUp() {
        sample1 = Course()
        sample2 = Course()
        sample3 = Course()

        sample1.setCourseName("u")
        sample1.setCourseTerm("B")
        sample1.setStartTime("1")
        sample1.setEndTime("2")
        sample1.setLocation("ss")
        sample1.setRecurrance(mutableListOf("th", "f"))

        sample3.setCourseName("dd")
        sample3.setCourseTerm("C")
        sample3.setStartTime("23 : 7")
        sample3.setEndTime("16 : 7")
        sample3.setLocation("ll")
        sample3.setRecurrance(mutableListOf("T", "F", "W", "Th"))
    }

    @Test
    fun getCourseName() {
        assertThat(sample1.getCourseName(), `is`("u"))
        assertThat(sample3.getCourseName(), `is`("dd"))
    }

    @Test
    fun setCourseName() {
        sample2.setCourseName("bbbb")
        assertThat(sample2.getCourseName(), `is`("bbbb"))
    }

    @Test
    fun getLocation() {
        assertThat(sample1.getLocation(), `is`("ss"))
        assertThat(sample3.getLocation(), `is`("ll"))
    }

    @Test
    fun setLocation() {
        sample2.setLocation("xxx")
        assertThat(sample2.getLocation(), `is`("xxx"))
    }

    @Test
    fun getStartTime() {
        assertThat(sample1.getStartTime(), `is`("1"))
        assertThat(sample3.getStartTime(), `is`("23 : 7"))
    }

    @Test
    fun setStartTime() {
        sample2.setStartTime("00:30")
        assertThat(sample2.getStartTime(), `is`("00:30"))
    }

    @Test
    fun getEndTime() {
        assertThat(sample1.getEndTime(), `is`("2"))
        assertThat(sample3.getEndTime(), `is`("16 : 7"))
    }

    @Test
    fun setEndTime() {
        sample2.setEndTime("11:11")
        assertThat(sample2.getEndTime(), `is`("11:11"))
    }

    @Test
    fun getRecurrance() {
        assertThat(sample1.getRecurrance(), `is`(mutableListOf("th", "f")))
        assertThat(sample3.getRecurrance(), `is`(mutableListOf("T", "F", "W", "Th")))
    }

    @Test
    fun setRecurrance() {
        sample2.setRecurrance(mutableListOf("a", "b", "c"))
        assertThat(sample2.getRecurrance(), `is`(mutableListOf("a", "b", "c")))
    }

    @Test
    fun getCourseTerm() {
        assertThat(sample1.getCourseTerm(), `is`("B"))
        assertThat(sample3.getCourseTerm(), `is`("C"))
    }

    @Test
    fun setCourseTerm() {
        sample2.setCourseTerm("A")
        assertThat(sample2.getCourseTerm(), `is`("A"))
    }
}