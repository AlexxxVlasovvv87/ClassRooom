package com.example.classrooom.game

import com.example.classrooom.game.GameAlgorithm
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.classrooom.R
import com.example.classrooom.game.MainGameActivity
import com.example.classrooom.game.QuickEyeGame
import com.example.classrooom.data.SharedPreferencesUtil
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.classrooom.activities.StudentActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.example.classrooom.activities.LevelUpActivity
import android.widget.TextView

class MainGameActivity : GameAlgorithm() {
    private var flipCount = 0
    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameplay_layout)
        Log.d(LOG_TAG, "onCreate")
        numberOfCards = 16
        gameLogic = QuickEyeGame((numberOfCards + 1) / 2)
        init()
        levelNumber = SharedPreferencesUtil.getUserLevel(this)
        levelNumTextView.text = resources.getText(R.string.lvl).toString() + " " + levelNumber
        setClick(false, 1) // time for becoming cards not clickable
        appearanceOfCards() // cards start to appear one by one
        openCardsRandomly() // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance) // delay of start of the game
        menuButton.setOnClickListener {
            val intent = Intent(this@MainGameActivity, StudentActivity::class.java)
            startActivity(intent)
        }
        val buttonClicks = View.OnClickListener { v ->
            val myAnim = AnimationUtils.loadAnimation(this@MainGameActivity, R.anim.alpha)
            v.startAnimation(myAnim)
            if (id != v.id) {
                flipCount += 1
                id = v.id
            }
            flipsCountView.text = "Flips: $flipCount"
            pointsView.text = "Points: " + gameLogic.mistakePoints
            gameLogic.chooseCard(getIndex(v.id))
            updateViewFromModel()
            if (gameLogic.checkForAllMatchedCards()) {
                levelNumber += 1
                val intent = Intent(this@MainGameActivity, LevelUpActivity::class.java)
                intent.putExtra("flips", flipCount)
                intent.putExtra("points", gameLogic.mistakePoints)
                intent.putExtra("activity", true)
                startActivity(intent)
            }
        }
        for (index in 0 until numberOfCards) {
            val btn = cards[index]
            if (btn.id - convertIdToIndex == index) btn.setOnClickListener(buttonClicks)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "onPause")
        SharedPreferencesUtil.saveUserLevel(this@MainGameActivity, levelNumber)
    }

    private fun init() {
        menuButton = findViewById(R.id.menuButton)
        restartButton = findViewById(R.id.restartButton)
        levelNumTextView = findViewById(R.id.levelTextView)
        flipsCountView = findViewById(R.id.flipsCountView)
        pointsView = findViewById(R.id.pointsView)
        flipsCountView.text = resources.getText(R.string.flips_0).toString() + " 0"
        pointsView.text = resources.getText(R.string.points_0).toString() + " 0"
        cards.add(findViewById<View>(R.id.button_00) as Button)
        cards.add(findViewById<View>(R.id.button_01) as Button)
        cards.add(findViewById<View>(R.id.button_02) as Button)
        cards.add(findViewById<View>(R.id.button_03) as Button)
        cards.add(findViewById<View>(R.id.button_04) as Button)
        cards.add(findViewById<View>(R.id.button_05) as Button)
        cards.add(findViewById<View>(R.id.button_06) as Button)
        cards.add(findViewById<View>(R.id.button_07) as Button)
        cards.add(findViewById<View>(R.id.button_08) as Button)
        cards.add(findViewById<View>(R.id.button_09) as Button)
        cards.add(findViewById<View>(R.id.button_10) as Button)
        cards.add(findViewById<View>(R.id.button_11) as Button)
        cards.add(findViewById<View>(R.id.button_12) as Button)
        cards.add(findViewById<View>(R.id.button_13) as Button)
        cards.add(findViewById<View>(R.id.button_14) as Button)
        cards.add(findViewById<View>(R.id.button_15) as Button)
        cards.add(findViewById<View>(R.id.button_16) as Button)
        cards.add(findViewById<View>(R.id.button_17) as Button)
        cards.add(findViewById<View>(R.id.button_18) as Button)
        cards.add(findViewById<View>(R.id.button_19) as Button)
        restartButton.visibility = View.INVISIBLE
    }

    companion object {
        private val LOG_TAG = MainGameActivity::class.java.simpleName
        private var levelNumber = 0
    }
}