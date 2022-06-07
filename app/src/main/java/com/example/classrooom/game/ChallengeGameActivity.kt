package com.example.classrooom.game

import com.example.classrooom.game.GameAlgorithm
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.animation.Animation
import com.example.classrooom.R
import com.example.classrooom.game.ChallengeGameActivity
import com.example.classrooom.info.Literals
import android.content.Intent
import android.graphics.Color
import com.example.classrooom.activities.LevelUpActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.example.classrooom.game.QuickEyeGame
import android.widget.TextView
import com.example.classrooom.activities.StudentActivity
import com.example.classrooom.data.DataBaseHelper
import android.widget.EditText
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.example.classrooom.info.Post
import java.util.*

class ChallengeGameActivity : GameAlgorithm() {
    private var handler: Handler? = null
    private var buffTime = 0L
    private var millisecTime: Long = 0
    private var levelNumber = 0
    private var flipCount = 0
    private var isReset = false
    private var mDatabase: DatabaseReference? = null
    private val user = FirebaseAuth.getInstance().currentUser
    var onCardsPushed = View.OnClickListener { v ->
        val myAnim = AnimationUtils.loadAnimation(this@ChallengeGameActivity, R.anim.alpha)
        if (id != v.id) {
            flipCount++
            amountOfFlips++
            id = v.id
        }
        flipsCountView.text = resources.getText(R.string.flips_0).toString() + " " + flipCount
        pointsView.text =
            resources.getText(R.string.points_0).toString() + " " + gameLogic.mistakePoints
        gameLogic.chooseCard(getIndex(v.id))
        updateViewFromModel()
        v.startAnimation(myAnim)
        if (gameLogic.checkForAllMatchedCards()) {
            Literals.points += Math.abs(gameLogic.mistakePoints) + flipCount
            allMistakes += gameLogic.mistakePoints
            if (levelNumber < Literals.maxLevel) {
                buffTime += millisecTime
                handler!!.removeCallbacks(runnable)
                val intent = Intent(this@ChallengeGameActivity, LevelUpActivity::class.java)
                intent.putExtra("game_reset", isReset)
                intent.putExtra("flips", flipCount)
                intent.putExtra("points", gameLogic.mistakePoints)
                startActivity(intent)
            } else {
                buffTime += millisecTime
                handler!!.removeCallbacks(runnable)
                showDialogModeSelector()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameplay_layout)
        mDatabase = FirebaseDatabase.getInstance().reference
        val bundle = intent.extras
        if (bundle != null) isReset = bundle.getBoolean("game_reset")
        gameReset(isReset, bundle)
        gameLogic = QuickEyeGame((numberOfCards + 1) / 2)
        init()
        gameSetUp()
        startTimer()
        initButtonsOnClick()
    }

    private fun gameReset(reset: Boolean, bundle: Bundle?) {
        if (reset) {
            levelNumber = Literals.getLevelNumber(false)
            numberOfCards = Literals.getNumberOFButtons(false)
            Literals.points = 0
            amountOfFlips = 0
            allMistakes = 0
            speed = 0
        } else {
            levelNumber = Literals.getLevelNumber(true)
            numberOfCards = Literals.getNumberOFButtons(true)
            if (bundle != null) speed = bundle.getInt("speed")
        }
    }

    private fun gameSetUp() {
        setClick(false, 1) // time for becoming cards not clickable
        appearanceOfCards() // cards start to appear one by one
        openCardsRandomly() // cards start opening randomly
        setClick(true, literals.delayForFirstAppearance + speed) // delay of start of the game
    }

    private fun startTimer() {
        Handler().postDelayed({
            handler = Handler()
            handler!!.postDelayed(runnable, 0)
            startTime = SystemClock.uptimeMillis()
        }, (literals.delayForFirstAppearance + speed).toLong())
    }

    private fun initButtonsOnClick() {
        for (i in 0 until numberOfCards) {
            val btn = cards[i]
            if (btn.id - convertIdToIndex == i) btn.setOnClickListener(onCardsPushed)
        }
    }

    private lateinit var stopWatchText: TextView
    private var seconds = 0
    private var minutes = 0
    private var milliSecs = 0
    private var startTime: Long = 0
    private var resetTime: Long = 0
    private fun init() {
        levelNumTextView = findViewById(R.id.levelTextView)
        flipsCountView = findViewById(R.id.flipsCountView)
        stopWatchText = findViewById(R.id.stopWatchText)
        pointsView = findViewById(R.id.pointsView)
        flipsCountView.text = resources.getText(R.string.flips_0).toString() + " 0"
        pointsView.text = resources.getText(R.string.points_0).toString() + " 0"
        cards.add(findViewById(R.id.button_00))
        cards.add(findViewById(R.id.button_01))
        cards.add(findViewById(R.id.button_02))
        cards.add(findViewById(R.id.button_03))
        cards.add(findViewById(R.id.button_04))
        cards.add(findViewById(R.id.button_05))
        cards.add(findViewById(R.id.button_06))
        cards.add(findViewById(R.id.button_07))
        cards.add(findViewById(R.id.button_08))
        cards.add(findViewById(R.id.button_09))
        cards.add(findViewById(R.id.button_10))
        cards.add(findViewById(R.id.button_11))
        cards.add(findViewById(R.id.button_12))
        cards.add(findViewById(R.id.button_13))
        cards.add(findViewById(R.id.button_14))
        cards.add(findViewById(R.id.button_15))
        cards.add(findViewById(R.id.button_16))
        cards.add(findViewById(R.id.button_17))
        cards.add(findViewById(R.id.button_18))
        cards.add(findViewById(R.id.button_19))
        millisecTime = 0L
        startTime = 0L
        buffTime = 0L
        resetTime = 0L
        seconds = 0
        minutes = 0
        milliSecs = 0
        stopWatchText.setText("00:00:00")
        levelNumTextView.text = resources.getText(R.string.lvl).toString() + " " + levelNumber
    }

    fun openHomeMenu(view: View?) {
        val replyIntent = Intent(this@ChallengeGameActivity, StudentActivity::class.java)
        finish()
        replyIntent.putExtra("game_reset", true)
        startActivity(replyIntent)
    }

    fun restartGame(view: View?) {
        val intent = intent
        finish()
        intent.putExtra("game_reset", true)
        startActivity(intent)
    }

    private val runnable: Runnable = object : Runnable {
        @SuppressLint("DefaultLocale", "SetTextI18n")
        override fun run() {
            millisecTime = SystemClock.uptimeMillis() - startTime
            resetTime = buffTime + millisecTime
            seconds = (resetTime / 1000).toInt()
            minutes = seconds / 60
            seconds = seconds % 60
            milliSecs = (resetTime % 1000).toInt()
            stopWatchText!!.text = ("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSecs))
            handler!!.postDelayed(this, 0)
        }
    }

    private fun round(digit: Int): Double {
        var result = Literals.maximumPoints / digit
        result *= 10000.0
        val roundRes = Math.round(result).toInt()
        return roundRes.toDouble() / 100
    }

    var dataBaseHelper = DataBaseHelper(this, "GameRes", null, 1)
    private fun showDialogModeSelector() {
        val LOG_TAG_DB = "DataBase"
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_win)
        val nameEditText = dialog.findViewById<EditText>(R.id.groupEditText)
        nameEditText.requestFocus()
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            nameEditText.hint = user.displayName
        }
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        dialog.findViewById(R.id.backBut3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = nameEditText.getText().toString().isEmpty()?
//                        user.getDisplayName() : nameEditText.getText().toString();
//
//                final String uid = getUid();
//
//                addPost(uid, name);
//
//                ContentValues contentValues = new ContentValues();
//                SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
//
//                Log.d(LOG_TAG_DB, "--- INSERT in the table: ---");
//                contentValues.put("Name", name);
//                contentValues.put("Percents", round(Literals.points));
//                contentValues.put("Flips", amountOfFlips);
//                contentValues.put("Points", allMistakes);
//                long rowID = database.insert("GameRes", null, contentValues);
//                Log.d(LOG_TAG_DB, "row inserted, ID = " + rowID);
//
//                nameEditText.clearFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                dataBaseHelper.close();
//                Intent intent = new Intent(ChallengeGameActivity.this, InfoActivity.class);
//                intent.putExtra("Results", true);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    private fun addPost(userId: String, username: String) {
        mDatabase!!.child("users").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (user == null) {
                    Log.e(LOG_TAG, "User $userId is unexpectedly null")
                    Toast.makeText(
                        this@ChallengeGameActivity, "Error: could not fetch user.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    writeNewPost(userId, username, round(Literals.points).toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(LOG_TAG, "getUser:onCancelled", databaseError.toException())
            }
        })
    }

    private fun writeNewPost(userId: String, name: String, percents: String) {
        val key = mDatabase!!.child("posts").push().key
        val post = Post(userId, name, percents)
        val postValues = post.toMap()
        val childUpdates: MutableMap<String, Any> = HashMap()
        childUpdates["/posts/$key"] = postValues
        childUpdates["/user-posts/$userId/$key"] = postValues
        mDatabase!!.updateChildren(childUpdates)
        Log.d(LOG_TAG, postValues.toString())
    }

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    companion object {
        private val LOG_TAG = ChallengeGameActivity::class.java.simpleName
        private var amountOfFlips = 0
        private var allMistakes = 0
    }
}