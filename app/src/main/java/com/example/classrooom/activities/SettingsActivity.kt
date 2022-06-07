package com.example.classrooom.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classrooom.R
import android.content.Intent
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.data.SharedPreferencesUtil
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import com.example.classrooom.activities.SettingsActivity
import java.lang.Exception

class SettingsActivity : AppCompatActivity() {
    private var backButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        init()
        backButton!!.setOnClickListener { v: View? ->
            val intent = Intent(this@SettingsActivity, StudentActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun init() {
        backButton = findViewById(R.id.backButton)
        val complexity = SharedPreferencesUtil.getComplexity(this)
        val theme = SharedPreferencesUtil.getTheme(this)
        radioDefaultSelect(complexity, theme)
    }

    @SuppressLint("NonConstantResourceId")
    fun changeComplexity(view: View) {
        val checked = (view as RadioButton).isChecked
        var chosen: Byte = -1
        when (view.getId()) {
            R.id.easy_compl -> if (checked) chosen = 0
            R.id.normal_compl -> if (checked) chosen = 1
            R.id.hard_compl -> if (checked) chosen = 2
            else -> {
            }
        }
        try {
            SharedPreferencesUtil.saveComplexity(this@SettingsActivity, chosen.toInt())
            Log.d(LOG_TAG, "complexity changed")
        } catch (e: Exception) {
            Log.e(LOG_TAG, "complexity not changed")
            e.printStackTrace()
        }
    }

    private fun radioDefaultSelect(compl: Int, theme: Int) {
        val easy = findViewById<RadioButton>(R.id.easy_compl)
        val normal = findViewById<RadioButton>(R.id.normal_compl)
        val hard = findViewById<RadioButton>(R.id.hard_compl)
        val animals = findViewById<RadioButton>(R.id.animals)
        val cars = findViewById<RadioButton>(R.id.cars)
        val food = findViewById<RadioButton>(R.id.food)
        val random = findViewById<RadioButton>(R.id.everything)
        when (compl) {
            0 -> easy.isChecked = true
            1 -> normal.isChecked = true
            2 -> hard.isChecked = true
            else -> {
            }
        }
        when (theme) {
            0 -> animals.isChecked = true
            1 -> cars.isChecked = true
            2 -> food.isChecked = true
            3 -> random.isChecked = true
            else -> {
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    fun changeTheme(view: View) {
        val checked = (view as RadioButton).isChecked
        var chosen: Byte = -1
        when (view.getId()) {
            R.id.animals -> if (checked) chosen = 0
            R.id.cars -> if (checked) chosen = 1
            R.id.food -> if (checked) chosen = 2
            R.id.everything -> if (checked) chosen = 3
            else -> {
            }
        }
        try {
            SharedPreferencesUtil.saveTheme(this@SettingsActivity, chosen.toInt())
            Log.d(LOG_TAG, "theme chosen")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(LOG_TAG, "error in selecting theme")
        }
    }

    companion object {
        private val LOG_TAG = SettingsActivity::class.java.simpleName
    }
}