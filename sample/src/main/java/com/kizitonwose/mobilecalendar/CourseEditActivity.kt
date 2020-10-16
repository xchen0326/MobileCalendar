package com.kizitonwose.mobilecalendar

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

//import database.StudentTypeConverters

private const val TAG = "SaveActivity"

class CourseEditActivity : AppCompatActivity() {

    private lateinit var stuViewModel: StudentViewModel

    var converter = Converter()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_edit)
        Log.d(TAG, "onCreate() called")

        stuViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        nameET = findViewById(R.id.cName)
        locET = findViewById(R.id.cLocation)
        sTimeP = findViewById(R.id.sTime)
        eTimeP = findViewById(R.id.eTime)
        suC = findViewById(R.id.Su)
        moC = findViewById(R.id.Mo)
        tuC = findViewById(R.id.Tu)
        weC = findViewById(R.id.We)
        thC = findViewById(R.id.Th)
        frC = findViewById(R.id.Fr)
        saC = findViewById(R.id.Sa)
        AC = findViewById(R.id.A)
        BC = findViewById(R.id.B)
        CC = findViewById(R.id.C)
        DC = findViewById(R.id.D)
        saveBtn = findViewById(R.id.save)
        cancelBtn = findViewById(R.id.cancel)
        sText = findViewById(R.id.textView7)
        eText = findViewById(R.id.textView8)

        val today: String = intent.getStringExtra("DATE") ?: "invalid date"

        //time picker for entering start time
        sTimeP.setOnClickListener{
            val c = Calendar.getInstance()
            val ampm = c.get(Calendar.AM_PM)
            var hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)
            if (ampm == Calendar.AM){
                if(hour == 12){
                    hour = 0
                }
            }else{
                if(hour != 12){
                    hour += 12
                }
            }

            val tpd = TimePickerDialog(
                this,
                { _, h, m ->
                    sTimeP.text = String.format("$h : $m")
                },
                hour,
                minute,
                true
            )
            tpd.show()
        }


        //time picker for entering end time.
        eTimeP.setOnClickListener{
            val c = Calendar.getInstance()
            val ampm = c.get(Calendar.AM_PM)
            var hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)
            if (ampm == Calendar.AM){
                if(hour == 12){
                    hour = 0
                }
            }else{
                if(hour != 12){
                    hour += 12
                }
            }

            val tpd = TimePickerDialog(
                this,
                { _, h, m ->
                    eTimeP.text = String.format("$h : $m")
                },
                hour,
                minute,
                true
            )
            tpd.show()
        }

        //Return to the CalendarFragment
        cancelBtn.setOnClickListener{
            finish();
        }


        //Return to the CalendarFragment and save course to database
        saveBtn.setOnClickListener {
            if(checkValid()){
                //Construct Course
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
                var course = Course()
                var lod = mutableListOf<String>()
                course.setCourseName(nameET.text.toString())
                course.setLocation(locET.text.toString())
                course.setStartTime(sTimeP.text.toString())
                course.setEndTime(eTimeP.text.toString())



                for(i in arrayOf(suC, moC, tuC, saC, frC, weC, thC)){
                    if(i.isChecked){
                        lod.add(i.text.toString())
                    }
                }
                course.setRecurrance(lod)
                for(i in arrayOf(AC, BC, CC, DC)){ //only one will be chosen
                    if(i.isChecked){
                        course.setCourseTerm(i.text.toString())
                        break
                    }
                }

                //check username then save the course into the database under the user.
                val user_name = HomeActivity.instance?.user_name
                val pass_word = HomeActivity.instance?.pass_word
                if (user_name != null&&pass_word != null) {
                    val courses = stuViewModel.getStudentCourses(user_name)
                    if (!courses.isNullOrEmpty()) {
                        var loc = converter.toListCourse(courses)
                        loc?.add(course)
                        println(courses.toString())
                        var str = converter.fromListCourse(loc)
                        if(str != null){
                            val student = Student(user_name, pass_word, str)
                            stuViewModel.updateStudent(student)
                        }else{
                            Toast.makeText(this, "Failed to add course in!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        var str2 = converter.fromCourse(course)
                        if(str2 != null){
                            val student = Student(user_name, pass_word, str2)
                            stuViewModel.updateStudent(student)
                        }else{
                            Toast.makeText(this, "Failed to add course in!", Toast.LENGTH_SHORT).show()
                        }

                    }
                }



                //return the course details back to CalendarFragment.
                val data = Intent().apply {
                    putExtra("SUCCESS", 1)
                    putExtra("COURSE_NAME", course.getCourseName())
                    putExtra("LOCATION", course.getLocation())
                    putExtra("START_TIME", course.getStartTime())
                    putExtra("END_TIME", course.getEndTime())
                    putExtra("TERM", course.getCourseTerm())
                    val lodArray: ArrayList<String> = ArrayList(lod)
                    putExtra("DAY_IN_WEEK",lodArray)

                }
                setResult(Activity.RESULT_OK, data)
                finish()

            }else{
                Toast.makeText(this, "Invalid Inputs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Check if all fields have valid entry
    fun checkValid() : Boolean{
        if (nameET.text.isEmpty() || locET.text.isEmpty() || sTimeP.text.isEmpty() || eTimeP.text.isEmpty()){
            return false
        }
        if(nameET.text.contains(",") || nameET.text.contains(";") || nameET.text.contains("/") || nameET.text.contains(" ") ||
            locET.text.contains(",") || locET.text.contains(";") || locET.text.contains("/") || locET.text.contains(" ")){
            Toast.makeText(this, "Please do not contain ',' ';' '/' ' ' in course name or location", Toast.LENGTH_LONG).show()
            return false
        }
        return (suC.isChecked || moC.isChecked || tuC.isChecked || saC.isChecked || frC.isChecked || weC.isChecked || thC.isChecked) && (AC.isChecked || BC.isChecked || CC.isChecked || DC.isChecked)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}