package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kizitonwose.mobilecalendar.Course
import com.kizitonwose.mobilecalendar.Student
import java.util.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM table_student")
    fun getStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM table_student WHERE username=(:username)")
    fun getStudent(username: String): Student?

    @Query("SELECT password FROM table_student WHERE username=(:username)")
    fun getStudentPassword(username: String): String?

    @Query("SELECT course FROM table_student WHERE username=(:username)")
    fun getStudentCourses(username: String): String?

    @Update
    fun updateStudent(student: Student)

    @Insert
    fun addStudent(student: Student)

}