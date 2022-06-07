package com.example.classrooom.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classrooom.R
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.example.classrooom.models.Score

class MathActivity : AppCompatActivity(), View.OnClickListener {
    var b1 = false
    var b2 = false
    var b3 = false
    var b4 = false
    var progress_bar = 0
    var all_bar = 0
    var randomer = 0
    var res = 0
    var res1 = 0
    var res2 = 0
    var res3 = 0
    var internal_score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.math_lay)
        algorithmGenerateFiguration()
    }

    @SuppressLint("SetTextI18n")
    fun algorithmGenerateFiguration() {
        val text = findViewById<View>(R.id.example) as TextView
        val progrtext = findViewById<View>(R.id.flipsCountView6) as TextView
        val alltext = findViewById<View>(R.id.flipsCountView5) as TextView
        val btn1 = findViewById<View>(R.id.play_button3) as Button
        val btn2 = findViewById<View>(R.id.play_button4) as Button
        val btn3 = findViewById<View>(R.id.play_button5) as Button
        val btn4 = findViewById<View>(R.id.play_button6) as Button
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        alltext.text = "Всего $all_bar"
        progrtext.text = "Баллы $progress_bar"
        val num_1: Int
        val num_2: Int
        val operation: Int
        var res_str: String? = ""
        num_1 = (Math.random() * 99 + 1).toInt()
        num_2 = (Math.random() * 99 + 1).toInt()
        operation = (Math.random() * 3).toInt()
        res_str += Integer.toString(num_1)
        when (operation) {
            0 -> {
                res = num_1 + num_2
                res_str += " + "
            }
            1 -> {
                res = num_1 - num_2
                res_str += " - "
            }
            2 -> {
                res = num_1 * num_2
                res_str += " * "
            }
            3 -> {
                res = num_1 / num_2
                res_str += " / "
            }
            else -> {
            }
        }
        res_str += "$num_2 = ?"
        text.text = res_str
        res1 = (Math.random() * 199 + 1).toInt()
        res2 = (Math.random() * 199 + 1).toInt()
        res3 = (Math.random() * 199 + 1).toInt()
        randomer = (Math.random() * 3).toInt()
        when (randomer) {
            0 -> {
                btn1.text = Integer.toString(res)
                btn2.text = Integer.toString(res1)
                btn3.text = Integer.toString(res2)
                btn4.text = Integer.toString(res3)
                b1 = true
            }
            1 -> {
                btn1.text = Integer.toString(res1)
                btn2.text = Integer.toString(res)
                btn3.text = Integer.toString(res2)
                btn4.text = Integer.toString(res3)
                b2 = true
            }
            2 -> {
                btn1.text = Integer.toString(res2)
                btn2.text = Integer.toString(res1)
                btn3.text = Integer.toString(res)
                btn4.text = Integer.toString(res3)
                b3 = true
            }
            3 -> {
                btn1.text = Integer.toString(res3)
                btn2.text = Integer.toString(res1)
                btn3.text = Integer.toString(res2)
                btn4.text = Integer.toString(res)
                b4 = true
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View) {
        var isGoodResult = false
        when (v.id) {
            R.id.play_button3 -> {
                if (b1) {
                    progress_bar++
                    isGoodResult = true
                } else {
                    progress_bar--
                }
                all_bar++
                algorithmGenerateFiguration()
            }
            R.id.play_button4 -> {
                if (b2) {
                    progress_bar++
                    isGoodResult = true
                } else {
                    progress_bar--
                }
                all_bar++
                algorithmGenerateFiguration()
            }
            R.id.play_button5 -> {
                if (b3) {
                    progress_bar++
                    isGoodResult = true
                } else {
                    progress_bar--
                }
                all_bar++
                algorithmGenerateFiguration()
            }
            R.id.play_button6 -> {
                if (b4) {
                    progress_bar++
                    isGoodResult = true
                } else {
                    progress_bar--
                }
                all_bar++
                algorithmGenerateFiguration()
            }
            else -> {
            }
        }
        updateFirebase(isGoodResult)
    }

    private fun updateFirebase(isGoodResult: Boolean) {
        val pointsDelta: Int
        if (isGoodResult) {
            internal_score++
            pointsDelta = 1
        } else {
            internal_score = 0
            pointsDelta = -1
        }
        var delta = 0
        if (internal_score > 2) {
            delta = 1
            internal_score = 0
        }
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val scoreRef = FirebaseDatabase.getInstance().getReference("UserScores").child(uid)
        val finalDelta = delta
        scoreRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            var score = dataSnapshot.getValue(Score::class.java)
            if (score != null) {
                score.progress += finalDelta
                score.points += pointsDelta
            } else {
                score = Score(finalDelta, pointsDelta)
            }
            scoreRef.setValue(score)
        }
    }
}