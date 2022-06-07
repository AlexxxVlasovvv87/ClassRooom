package com.example.classrooom.activities

import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.classrooom.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import com.example.classrooom.activities.SignInActivity
import com.example.classrooom.activities.SettingsActivity
import com.example.classrooom.activities.AdminActivity

class AdminActivity : AppCompatActivity() {
    private var myAnim: Animation? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        myAnim = AnimationUtils.loadAnimation(this, R.anim.scale)
        checkLogin()
    }

    private fun checkLogin() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            openSignInView()
        }
    }

    fun openSignInView() {
        val intent = Intent(this@AdminActivity, SignInActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun openSettingsView(view: View) {
        view.startAnimation(myAnim)
        val intent = Intent(this@AdminActivity, SettingsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        private val LOG_TAG = AdminActivity::class.java.simpleName
    }
}