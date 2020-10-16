package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kizitonwose.mobilecalendar.Student

@Database(entities = [Student::class], version = 1)
//@TypeConverters(StudentTypeConverters::class)
abstract class StudentDatabase : RoomDatabase(){
    private val dbName = "user"
    private lateinit var studentDatabase: StudentDatabase

    abstract fun studentDao() : StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase?= null
        fun getDatabase(context: Context): StudentDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "user_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
//    fun getStudentDatabase(context: Context){
//        if (studentDatabase == null){
//            studentDatabase = Room.databaseBuilder(
//                context.applicationContext,
//                StudentDatabase::class.java,
//                dbName
//            ).build()
//        }
//    }
    }

}