package com.kizitonwose.mobilecalendar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import database.StudentDao
import database.StudentDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "student_database"//-?_?

class StudentRepository(private val studentDao: StudentDao){
    fun addStudent(student: Student){
        studentDao.addStudent(student)
    }

    fun getStudent(username: String):Student?{
        return studentDao.getStudent(username)
    }

    fun getStudentCourses(username: String):String?{
        return studentDao.getStudentCourses(username)
    }

    fun updateStudent(student: Student){
        return studentDao.updateStudent(student)
    }

    fun getStudentPassword(username: String):String?{
        return studentDao.getStudentPassword(username)
    }

//    private val database : StudentDatabase = Room.databaseBuilder(
//        context.applicationContext,
//        StudentDatabase::class.java,
//        DATABASE_NAME
//    ).build()
//
//
//    private val executor = Executors.newSingleThreadExecutor()
//    private val studentDao = database.studentDao()
//
//    fun getStudents() : LiveData<List<Student>> = studentDao.getStudents()
//    fun getStudent(id: UUID) : LiveData<Student?> = studentDao.getStudent(id)
//
//    fun updateStudent(student: Student){
//        executor.execute{studentDao.updateStudent(student)}
//    }
//
//    fun addStudent(student: Student){
//        executor.execute{studentDao.addStudent(student)}
//    }
//
//    companion object {
//        private var INSTANCE: StudentRepository? = null
//
//        fun initialize(context: Context) {
//            if (INSTANCE == null) {
//                INSTANCE = StudentRepository(context)
//            }
//        }
//
//        fun get(): StudentRepository {
//            return INSTANCE ?:
//            throw IllegalStateException("StudentRepo must be initialized")
//        }
//
//        fun getstudentDao(): StudentDao? {
//            if (INSTANCE != null) {
//                return INSTANCE!!.studentDao
//            }
//            return null
//        }
//    }
}