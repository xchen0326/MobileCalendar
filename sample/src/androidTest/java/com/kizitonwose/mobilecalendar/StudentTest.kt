package com.kizitonwose.mobilecalendar

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class StudentTest {
    private lateinit var student1: Student
    private lateinit var student2: Student
    private lateinit var student3: Student
    private lateinit var sample1 : Course
    private lateinit var sample2 : Course
    private lateinit var sample3 : Course
    private lateinit var password1 : String
    private lateinit var password2 : String
    private lateinit var password3 : String
    private lateinit var username1 : String
    private lateinit var username2 : String
    private lateinit var username3 : String
    private lateinit var converter : Converter

    @Before
    fun setUp() {
        converter = Converter()
        sample1 = Course()
        sample2 = Course()
        sample3 = Course()
        username1 = "user1"
        username2 = "user2"
        username3 = "user3"
        password1 = "pass1"
        password2 = "pass2"
        password3 = "pass3"

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

        student1 = converter.fromListCourse(mutableListOf(sample1,sample2))?.let {
            Student(username1,password1,
                it
            )
        }!!
        student2 = converter.fromListCourse(mutableListOf(sample1,sample2,sample3))?.let {
            Student(username2,password2,
                it
            )
        }!!
        student3 = converter.fromListCourse(mutableListOf(sample3))?.let {
            Student(username3,password3,
                it
            )
        }!!
    }

    @Test
    fun getUsername() {
        assertThat(student1.username, `is`(username1))
        assertThat(student2.username, `is`(username2))
        assertThat(student3.username, `is`(username3))
    }

    @Test
    fun getPassword() {
        assertThat(student1.password, `is`(password1))
        assertThat(student2.password, `is`(password2))
        assertThat(student3.password, `is`(password3))
    }

    @Test
    fun getCourse() {
        assertThat(student1.course, `is`(converter.fromListCourse(mutableListOf(sample1,sample2))))
        assertThat(converter.toListCourse(student2.course)?.size, `is`(3))
        assertThat(student3.course, `is`(converter.fromListCourse(mutableListOf(sample3))))
    }
}