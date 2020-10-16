package com.kizitonwose.mobilecalendar

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity


//import kotlinx.android.synthetic.main.course_edit_fragment.*

lateinit var nameET:EditText
lateinit var locET:EditText
lateinit var sTimeP:Button
lateinit var eTimeP:Button
lateinit var suC: CheckBox
lateinit var moC: CheckBox
lateinit var tuC: CheckBox
lateinit var weC: CheckBox
lateinit var thC: CheckBox
lateinit var frC: CheckBox
lateinit var saC: CheckBox
lateinit var AC: RadioButton
lateinit var BC: RadioButton
lateinit var CC: RadioButton
lateinit var DC: RadioButton
lateinit var saveBtn: Button
lateinit var cancelBtn: Button
lateinit var sText: TextView
lateinit var eText: TextView


class CourseFragment : Fragment(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.course_edit_fragment, container, false)
        nameET = view.findViewById(R.id.cName)
        locET = view.findViewById(R.id.cLocation)
        sTimeP = view.findViewById(R.id.sTime)
        eTimeP = view.findViewById(R.id.eTime)
        suC = view.findViewById(R.id.Su)
        moC = view.findViewById(R.id.Mo)
        tuC = view.findViewById(R.id.Tu)
        weC = view.findViewById(R.id.We)
        thC = view.findViewById(R.id.Th)
        frC = view.findViewById(R.id.Fr)
        saC = view.findViewById(R.id.Sa)
        AC = view.findViewById(R.id.A)
        BC = view.findViewById(R.id.B)
        CC = view.findViewById(R.id.C)
        DC = view.findViewById(R.id.D)
        saveBtn = view.findViewById(R.id.save)
        cancelBtn = view.findViewById(R.id.cancel)
        sText = view.findViewById(R.id.textView7)
        eText = view.findViewById(R.id.textView8)

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
                context,
                { _, h, m ->
                    sTimeP.text = String.format("$h : $m")
                },
                hour,
                minute,
                true
            )
            tpd.show()
        }

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
                context,
                { _, h, m ->
                    eTimeP.text = String.format("$h : $m")
                },
                hour,
                minute,
                true
            )
            tpd.show()
        }

        saveBtn.setOnClickListener {
            if(checkValid()){
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
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



            }else{
                Toast.makeText(context, "Invalid Inputs", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkValid() : Boolean{
        if (nameET.text.isEmpty() || locET.text.isEmpty() || sTimeP.text.isEmpty() || eTimeP.text.isEmpty()){
            return false
        }
        return (suC.isChecked || moC.isChecked || tuC.isChecked || saC.isChecked || frC.isChecked || weC.isChecked || thC.isChecked) && (AC.isChecked || BC.isChecked || CC.isChecked || DC.isChecked)
    }
}