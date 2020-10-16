package com.kizitonwose.mobilecalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders


lateinit var usrName: EditText
lateinit var password: EditText
lateinit var loginBtn: Button
lateinit var signupBtn: Button

class LoginFragment : Fragment() {

    private val studentViewModel : StudentViewModel by lazy {
        ViewModelProviders.of(this).get(StudentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        usrName = view.findViewById(R.id.userName)
        password = view.findViewById(R.id.passWord)
        loginBtn = view.findViewById(R.id.loginChangeFragment)
        signupBtn = view.findViewById(R.id.button2)

        return view
    }


    fun checkValidity(): Boolean{
        return !(usrName.text.isEmpty() || password.text.isEmpty())
    }
}