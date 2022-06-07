package com.example.classrooom.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.example.classrooom.utils.MyUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.activities.TeacherActivity
import com.example.classrooom.models.User
import java.lang.Exception

class SignInFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        Log.d(LOG_TAG, "onCreateView")
        val email = view.findViewById<TextView>(R.id.email)
        val password = view.findViewById<TextView>(R.id.password)
        val signInButton = view.findViewById<Button>(R.id.play_button)
        val backButton = view.findViewById<Button>(R.id.backBut3)
        backButton.setOnClickListener { view1: View? ->
            val navHostFragment = NavHostFragment.findNavController(this)
            navHostFragment.navigateUp()
        }
        signInButton.setOnClickListener { view1: View? ->
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (MyUtils.isValidUser(emailText, passwordText)) {
                logIn(emailText, passwordText)
            } else {
                Toast.makeText(
                    activity,
                    "Неправильно введены данные",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }

    private fun logIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { task: AuthResult ->
                val uid = task.user!!.uid
                val userRef = FirebaseDatabase.getInstance().getReference("Users")
                userRef.child(uid).get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
                    val user = dataSnapshot.getValue(
                        User::class.java
                    )
                    if (user == null) {
                        Toast.makeText(context, "Аккаунт удален", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                    } else if (user.isTeacher) {
                        toTeacherActivity()
                    } else {
                        toStudentActivity()
                    }
                }
            }.addOnFailureListener { e: Exception ->
            // TODO() Нужна локализованная информация об ошибке
            Toast.makeText(context, "Произошла ошибка:" + e.localizedMessage, Toast.LENGTH_LONG)
                .show()
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
        private val LOG_TAG = SignInFragment::class.java.simpleName
    }
}