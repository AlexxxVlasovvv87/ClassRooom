package com.example.classrooom.activities

import android.app.Activity
import com.example.classrooom.info.Variables
import android.os.Bundle
import com.example.classrooom.R
import android.annotation.SuppressLint
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.example.classrooom.models.Score
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.activities.LevelUpActivity
import com.example.classrooom.game.MainGameActivity
import com.example.classrooom.game.ChallengeGameActivity

class LevelUpActivity : Activity() {
    private val `var` = Variables()
    private var whichActivity = false
    private var isReset = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nextlvl_layout)
        val bundle = intent.extras!!
        isReset = bundle.getBoolean("game_reset")
        val flipsNum = bundle.getInt("flips")
        val points = bundle.getInt("points")
        whichActivity = bundle.getBoolean("activity")
        init(flipsNum, points)
    }

    @SuppressLint("SetTextI18n")
    private fun init(flipsNum: Int, points: Int) {
        val levelPassedTextView = findViewById<TextView>(R.id.levelPassedTextView)
        levelPassedTextView.text = """${resources.getText(R.string.matched_text1)}
$flipsNum ${resources.getText(R.string.matched_text2)}
$points ${resources.getText(R.string.matched_text3)}"""
        updateScore(points)
    }

    private fun updateScore(points: Int) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val userScore = FirebaseDatabase.getInstance().getReference("UserScores").child(uid)
        userScore.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            var score = dataSnapshot.getValue(Score::class.java)
            if (score == null) {
                score = Score(0, 0)
            }
            score.progress++
            score.points += points
            userScore.setValue(score)
        }
    }

    fun openHomeMenu(view: View?) {
        val replyIntent = Intent(this@LevelUpActivity, StudentActivity::class.java)
        finish()
        replyIntent.putExtra("game_reset", true)
        startActivity(replyIntent)
    }

    fun nextLevel(view: View?) {
        val intent: Intent
        intent = if (whichActivity) {
            Log.d(LOG_TAG, "main game mode opened")
            Intent(this@LevelUpActivity, MainGameActivity::class.java)
        } else {
            Log.d(LOG_TAG, "challenge game mode opened")
            Intent(this@LevelUpActivity, ChallengeGameActivity::class.java)
        }
        intent.putExtra("speed", `var`.setChange(!isReset))
        intent.putExtra("levelUp", true)
        startActivity(intent)
    }

    companion object {
        private val LOG_TAG = LevelUpActivity::class.java.simpleName
    }
}