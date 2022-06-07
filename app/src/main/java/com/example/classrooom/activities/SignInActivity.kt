package com.example.classrooom.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classrooom.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import android.content.Intent
import android.view.View
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.activities.AdminActivity
import com.example.classrooom.activities.TeacherActivity
import com.example.classrooom.models.User

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance().reference
        val splash = findViewById<ConstraintLayout>(R.id.splash)
        if (auth.currentUser != null && auth.currentUser!!.uid != null) {
            if (auth.currentUser!!.uid == "QUPYjGXra8SoXBZC5pj8UImK0oN2") {
                toAdminActivity()
            } else {
                dbRef.child("Users").child(auth.currentUser!!.uid).get()
                    .addOnSuccessListener { dataSnapshot: DataSnapshot ->
                        val user = dataSnapshot.getValue(
                            User::class.java
                        )
                        if (user!!.isTeacher) {
                            toTeacherActivity()
                        } else {
                            toStudentActivity()
                        }
                    }
            }
        } else {
            splash.visibility = View.GONE
        }
    }

    private fun toStudentActivity() {
        val intent = Intent(this, StudentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toTeacherActivity() {
        val intent = Intent(this, TeacherActivity::class.java)
        startActivity(intent)
        finish()
    }
}