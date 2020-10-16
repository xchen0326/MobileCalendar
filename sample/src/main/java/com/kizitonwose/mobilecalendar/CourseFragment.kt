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





//============================OBSOLETE========================================
//We decided to use CourseEditActivity instead of a Fragment.

