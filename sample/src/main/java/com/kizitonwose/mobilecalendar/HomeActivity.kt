package com.kizitonwose.mobilecalendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.mobilecalendar.databinding.HomeActivityBinding


class HomeActivity : AppCompatActivity() {

    internal lateinit var binding: HomeActivityBinding
    var manager = supportFragmentManager
    private lateinit var stuViewModel: StudentViewModel
    var saveId = 0
    var user_name = ""
    var pass_word = ""
    companion object{
        var instance: HomeActivity?=null
    }

    private val examplesAdapter = HomeOptionsAdapter {
        val fragment = it.createView()
        supportFragmentManager.beginTransaction()
            .run {
                return@run setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            .add(R.id.homeContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instance = this
        stuViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        setSupportActionBar(binding.homeToolbar)
        binding.examplesRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = examplesAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        setContentView(R.layout.activity_main)

        showLoginFragment()//shows the login page
//        val btn = findViewById<Button>(R.id.loginChangeFragment)
//        btn.setOnClickListener(){
//
//        }
        //showCourseFragment()
//        showCalendarFragment()
    }

    fun showLoginFragment(){
        val transaction = manager.beginTransaction()
        val frag = LoginFragment()
        transaction.replace(R.id.fragment_holder, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showCourseFragment(){
        val transaction = manager.beginTransaction()
        val frag = CourseFragment()
        transaction.replace(R.id.fragment_holder, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showCalendarFragment(){
        val transaction = manager.beginTransaction()
        val frag = CalendarFragment()
        transaction.replace(R.id.fragment_holder, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun freshCalendar(view: View){
//        var frg: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_holder)
        val transaction = manager.beginTransaction()
//        if (frg != null) {
//            transaction.detach(frg)
//            println("detach the view")
//        }
//        if (frg != null) {
//            transaction.attach(frg)
//            println("reattach the view")
//        }
        val frag = CalendarFragment()
        transaction.replace(R.id.fragment_holder, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun userLogin(view: View){
        val passwordText = findViewById<EditText>(R.id.passWord).text.toString()
        val usernameText = findViewById<EditText>(R.id.userName).text.toString()
        val password = stuViewModel.getStudentPassword(usernameText)
        if (password.equals(passwordText)) {
//            var courselist = mutableListOf<Course>()
//            val course = Course()
//            course.setCourseName("new courseName")
//            courselist.add(course)
//            val student = Student("Liumxiris", "kkkkk", courselist)
//            stuViewModel.updateStudent(student)

//            val intent = Intent(this@HomeActivity, CourseEditActivity::class.java)
//            intent.putExtra("USERNAME", usernameText)
//            intent.putExtra("PASSWORD", passwordText)
//            startActivity(intent)
            user_name = usernameText
            pass_word = passwordText
            showCalendarFragment()
        }
        else{
            Toast.makeText(this, "password incorrect", Toast.LENGTH_SHORT).show()
        }
//        showCourseFragment()
//        Toast.makeText(this, "login pressed.", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun registerUser(view: View){
        val username = findViewById<EditText>(R.id.userName).text.toString()
        val password = findViewById<EditText>(R.id.passWord).text.toString()
        if (!username.equals("")&&!password.equals("")) {
            val currentId = getCurrentId()
            saveId = currentId
            val student = Student(username, password)
            if (stuViewModel.getStudent(username)==null) {
                stuViewModel.addStudent(student)
                val settings: SharedPreferences
                settings = getSharedPreferences("ID_SAVING", Context.MODE_PRIVATE)
                //set the sharedpref
                //set the sharedpref
                val editor = settings.edit()
                editor.putInt("ID", saveId)
                editor.commit()
                Toast.makeText(this, "successfully registered! ${username}", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                Toast.makeText(this, "this username has been occupied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        else {
            Toast.makeText(this, "Please input things!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentId():Int{
        val settings: SharedPreferences
        settings = getSharedPreferences("ID_SAVING", Context.MODE_PRIVATE)

        //get the sharepref

        //get the sharepref
        val currentId = settings.getInt("ID", 0)
        return currentId+1
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
        val settings: SharedPreferences
        settings = getSharedPreferences("ID_SAVING", Context.MODE_PRIVATE)
        //set the sharedpref
        //set the sharedpref
        val editor = settings.edit()
        editor.putInt("ID", saveId)
        editor.commit()
    }
    override fun onBackPressed() {
        println("onBackPressed Called")
        showLoginFragment()
    }
}
