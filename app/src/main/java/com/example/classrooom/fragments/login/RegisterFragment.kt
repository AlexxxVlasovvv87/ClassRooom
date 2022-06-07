package com.example.classrooom.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import com.example.classrooom.utils.MyUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.activities.TeacherActivity
import com.example.classrooom.models.User

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    private var adminRegister = false
    private lateinit var email: EditText
    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var password: EditText
    private lateinit var secondPassword: EditText
    private lateinit var title: TextView
    lateinit var isTeacherBox: CheckBox
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        if (arguments != null && requireArguments().containsKey("AdminRegister")) {
            adminRegister = true
        }
        email = view.findViewById(R.id.email)
        name = view.findViewById(R.id.name)
        surname = view.findViewById(R.id.surname)
        password = view.findViewById(R.id.password)
        secondPassword = view.findViewById(R.id.secondpassword)
        val registerButton = view.findViewById<Button>(R.id.play_button)
        isTeacherBox = view.findViewById(R.id.acceptteacher)
        val backButton = view.findViewById<Button>(R.id.backBut2)
        title = view.findViewById(R.id.Text)
        if (adminRegister) setNames()
        backButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this).navigateUp()
        }
        registerButton.setOnClickListener { view1: View? ->
            registerCheck(
                email.getText().toString(),
                name.getText().toString(),
                surname.getText().toString(),
                password.getText().toString(),
                secondPassword.getText().toString(),
                isTeacherBox.isChecked()
            )
        }
        return view
    }

    private fun setNames() {
        email!!.hint = "E-mail пользователя"
        name!!.hint = "Имя пользователя"
        surname!!.hint = "Фамилия пользователя"
        title!!.text = "Зарегистрировать"
        isTeacherBox!!.text = "Это учитель"
    }

    private fun registerCheck(
        email: String, name: String, surname: String,
        password: String, secondPassword: String, isTeacher: Boolean
    ) {
        if (MyUtils.isValidUserRegister(email, name, surname, password, secondPassword)) {
            val user = User(email, name, surname, isTeacher)
            register(user, password)
        } else {
            Toast.makeText(
                activity,
                "Неправильно введены данные",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun register(user: User, password: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")
        val auth = FirebaseAuth.getInstance()
        user.email?.let {
            auth.createUserWithEmailAndPassword(it, password)
                .addOnSuccessListener { authResult: AuthResult ->
                    user.uid = authResult.user!!.uid
                    userRef.child(user.uid!!).setValue(user).addOnSuccessListener { aVoid: Void? ->
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email!!, password)
                        if (user.isTeacher) {
                            toTeacherActivity()
                        } else {
                            toStudentActivity()
                        }
                    }
                }
        }
    }

    private fun toStudentActivity() {
        val intent = Intent(requireActivity().application, StudentActivity::class.java)
        startActivity(intent)
    }

    private fun toTeacherActivity() {
        val intent = Intent(requireActivity().application, TeacherActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val LOG_TAG = RegisterFragment::class.java.simpleName
    }
}