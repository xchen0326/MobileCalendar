package com.kizitonwose.mobilecalendar

import android.app.Application
import androidx.lifecycle.*
import database.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class StudentViewModel(application: Application): AndroidViewModel(application){
    private val studentRepository: StudentRepository
 //   private val studentIdLiveData = MutableLiveData<UUID>()
//    private lateinit var passwordLiveData:LiveData<String?>
    var stuId = 1
    init {
        val studentDao = StudentDatabase.getDatabase(application).studentDao()
        studentRepository = StudentRepository(studentDao)

    }

    fun addStudent(student: Student){
        viewModelScope.launch (Dispatchers.IO){
            studentRepository.addStudent(student)
        }
        stuId += 1
    }

    fun updateStudent(student: Student){
        return studentRepository.updateStudent(student)
    }

    fun getStudentCourses(username: String): String? {
        return studentRepository.getStudentCourses(username)
    }

    fun getStudentPassword(username: String):String?{
        return studentRepository.getStudentPassword(username)
    }

    fun getStudent(username: String):Student?{
        return studentRepository.getStudent(username)
    }

//    val studentListLiveData = studentRepository.getStudents()
//    var studentLiveData: LiveData<Student?> =
//        Transformations.switchMap(studentIdLiveData){
//                studentId -> studentRepository.getStudent(studentId)
//        }
//
//    fun loadStudent(studentId: UUID){
//        studentIdLiveData.value = studentId
//    }
//
//    fun saveStudent(studentId: Student){
//        studentRepository.updateStudent(studentId)
//    }
//
//    fun newStudent(studentId: Student){
//        studentRepository.addStudent(studentId)
//    }
}