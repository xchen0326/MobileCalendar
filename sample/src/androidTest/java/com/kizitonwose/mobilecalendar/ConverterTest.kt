package com.kizitonwose.mobilecalendar

import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ConverterTest {
    private lateinit var sample1 : Course
    private lateinit var sample2 : Course
    private lateinit var sample3 : Course
    private lateinit var converter : Converter

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

        sample2.setCourseName("u")
        sample2.setCourseTerm("B")
        sample2.setStartTime("3")
        sample2.setEndTime("4")
        sample2.setLocation("ss")
        sample2.setRecurrance(mutableListOf("th", "f"))

        sample3.setCourseName("dd")
        sample3.setCourseTerm("B")
        sample3.setStartTime("23 : 7")
        sample3.setEndTime("16 : 7")
        sample3.setLocation("ll")
        sample3.setRecurrance(mutableListOf("T", "F", "W", "Th"))

        converter = Converter()
    }

    @Test
    fun fromListCourse() {
        assertThat(converter.fromListCourse(mutableListOf(sample1, sample2)),
            `is`("u,ss,1,2,th;f,B,/u,ss,3,4,th;f,B,/"))
        assertThat(converter.fromListCourse(mutableListOf(sample3)),
            `is`("dd,ll,23 : 7,16 : 7,T;F;W;Th,B,/"))
        assertThat(converter.fromListCourse(mutableListOf(sample1, sample2, sample3)),
            `is`("u,ss,1,2,th;f,B,/u,ss,3,4,th;f,B,/dd,ll,23 : 7,16 : 7,T;F;W;Th,B,/"))
    }

    @Test
    fun toListCourse() {
        assertThat(
            converter.toListCourse("u,ss,1,2,th;f,B,/u,ss,3,4,th;f,B,/dd,ll,23 : 7,16 : 7,T;F;W;Th,B,/")
                ?.get(1)
                ?.getCourseTerm(),
            `is`(mutableListOf(sample1, sample2, sample3)[1].getCourseTerm()))
        assertThat(converter.toListCourse("u,ss,1,2,th;f,B,/u,ss,3,4,th;f,B,/")
            ?.get(0)
            ?.getCourseName(),
            `is`(mutableListOf(sample1, sample2)[0].getCourseName()))
        assertThat(converter.toListCourse("u,ss,1,2,th;f,B,/")
            ?.get(0)
            ?.getEndTime(),
            `is`(mutableListOf(sample1)[0].getEndTime()))
    }

    @Test
    fun fromCourse() {
        assertThat(converter.fromCourse(sample1),`is`("u,ss,1,2,th;f,B,"))
        assertThat(converter.fromCourse(sample2),`is`("u,ss,3,4,th;f,B,"))
        assertThat(converter.fromCourse(sample3),`is`("dd,ll,23 : 7,16 : 7,T;F;W;Th,B,"))
    }

    @Test
    fun toCourse() {
        assertThat(converter.toCourse("u,ss,1,2,th;f,B,")?.getRecurrance(), `is`(sample1.getRecurrance()))
        assertThat(converter.toCourse("u,ss,3,4,th;f,B,")?.getCourseName(), `is`(sample2.getCourseName()))
        assertThat(converter.toCourse("dd,ll,23 : 7,16 : 7,T;F;W;Th,B,")?.getCourseTerm(), `is`(sample3.getCourseTerm()))
    }
}