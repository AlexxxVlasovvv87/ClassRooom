package com.example.classrooom.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.AdminActivity
import java.lang.Exception

class AdminSignInFragment : Fragment() {
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
            NavHostFragment.findNavController(this).navigateUp()
        }
        signInButton.setOnClickListener { view1: View? ->
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
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
            .addOnSuccessListener { task: AuthResult? ->
                if (email == "admin@main.com" && password == "admin123") {
                    val intent = Intent(requireActivity().application, AdminActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener { e: Exception ->
            // TODO() Нужна локализованная информация об ошибке
            Toast.makeText(context, "Произошла ошибка:" + e.localizedMessage, Toast.LENGTH_LONG)
                .show()
        }
    }

    companion object {
        private val LOG_TAG = AdminSignInFragment::class.java.simpleName
    }
}