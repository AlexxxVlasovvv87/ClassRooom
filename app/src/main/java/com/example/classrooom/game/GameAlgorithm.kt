package com.example.classrooom.game

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.classrooom.info.Literals
import com.example.classrooom.game.QuickEyeGame
import android.widget.TextView
import com.example.classrooom.R
import com.example.classrooom.data.SharedPreferencesUtil
import android.graphics.PorterDuff
import android.os.Handler
import android.view.View
import android.widget.Button
import com.example.classrooom.models.Card
import com.example.classrooom.models.Theme
import java.util.*

open class GameAlgorithm : AppCompatActivity() {
    protected var numberOfCards = 0
    private val colorsOfCards = ArrayList<Int>()
    protected var cards = ArrayList<Button>()
    protected var literals = Literals()
    protected lateinit var gameLogic: QuickEyeGame
    protected lateinit var menuButton: Button
    protected lateinit var restartButton: Button
    protected lateinit var levelNumTextView: TextView
    protected lateinit var flipsCountView: TextView
    protected lateinit var pointsView: TextView
    protected val convertIdToIndex = R.id.button_00
    protected var speed = 0
    protected var id = -1
    private fun setComplexity() {
        var numColors = 1
        val difficultyLevel = SharedPreferencesUtil.getComplexity(this)
        val buttonColors = resources.getIntArray(R.array.buttoncolors)
        val arrayOfColors = ArrayList<Int>()
        for (a in buttonColors) arrayOfColors.add(a)
        Collections.shuffle(arrayOfColors)
        when (difficultyLevel) {
            1 -> numColors = 2
            2 -> numColors = 3
        }
        for (i in 0 until numberOfCards) {
            val randomColor = Random().nextInt(numColors)
            colorsOfCards.add(i, arrayOfColors[randomColor])
        }
    }

    private fun getColorOfButtons(index: Int): Int {
        return colorsOfCards[index]
    }

    private fun circleCards() {
        for (index in 0 until numberOfCards - 1) {
            val k = index + 1
            val button = cards[index]
            button.postDelayed({
                button.background.setColorFilter(getColorOfButtons(k), PorterDuff.Mode.MULTIPLY)
                button.visibility = View.VISIBLE
            }, (literals.delayForFirstAppearance - 200).toLong())
        }
    }

    fun setClick(fl: Boolean?, delay: Int) {
        Handler().postDelayed({
            for (index in 0 until numberOfCards) cards[index].isClickable = fl!!
        }, delay.toLong())
    }

    fun appearanceOfCards() {
        setComplexity()
        for (index in 0 until numberOfCards) {
            val button = cards[index]
            button.background.setColorFilter(getColorOfButtons(index), PorterDuff.Mode.MULTIPLY)
            button.postDelayed(
                { button.visibility = View.VISIBLE },
                (literals.delayForFirstAppearance - 200).toLong()
            )
            literals.delayForFirstAppearance += literals.delayBetweenAppearance
        }
        if (SharedPreferencesUtil.getComplexity(this) == 2) circleCards()
    }

    fun openCardsRandomly() {
        val checkTheRepeat: MutableMap<Int, Boolean> = HashMap()
        for (index in 0 until numberOfCards) {
            checkTheRepeat[index] = false // all buttons haven't opened yet => false
        }
        val randArrOfFirstIndexes = ArrayList<Int>() // array of random sequence of cards' indexes
        for (index in 0 until numberOfCards) randArrOfFirstIndexes.add(index)
        Collections.shuffle(randArrOfFirstIndexes)
        if (numberOfCards == 4) {
            val deleteRandom = (Math.random() * 4).toInt()
            randArrOfFirstIndexes.removeAt(deleteRandom)
        } else {
            val secondRandArray =
                IntArray((Math.random() * (numberOfCards / 3) + numberOfCards / 3).toInt()) // random size [(numberOfCards / 4);(numberOfCards/2)]
            for (index in secondRandArray.indices) {
                var randomIndexOfFirstArray: Int
                do {
                    randomIndexOfFirstArray = (Math.random() * numberOfCards).toInt()
                } while (checkTheRepeat[randomIndexOfFirstArray]!!)
                secondRandArray[index] = randArrOfFirstIndexes[randomIndexOfFirstArray]
                checkTheRepeat[randomIndexOfFirstArray] = true
            }
            for (value in secondRandArray) randArrOfFirstIndexes.add(value)
            Collections.shuffle(randArrOfFirstIndexes)
            var index = 0
            do {
                if (randArrOfFirstIndexes[index] == randArrOfFirstIndexes[index + 1]) {
                    var random: Int
                    do {
                        random = (Math.random() * randArrOfFirstIndexes.size).toInt()
                    } while (random == index || random == index + 1)
                    val temp = randArrOfFirstIndexes[index + 1]
                    randArrOfFirstIndexes.add(index + 1, randArrOfFirstIndexes[random])
                    randArrOfFirstIndexes.add(random, temp)
                }
                index++
            } while (index < randArrOfFirstIndexes.size - 1)
        }
        outPutRandomly(randArrOfFirstIndexes)
    }

    fun outPutRandomly(array: ArrayList<Int>) {
        for (index in array.indices) {
            val randomButtonIndex = array[index]
            val finalBut = pressedButton(randomButtonIndex)
            finalBut.textSize = 43f
            finalBut.postDelayed({
                finalBut.text = gameLogic!!.cards[randomButtonIndex]?.let { getEmoji(it) } // opened
                finalBut.background.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            }, (literals.delayForFirstAppearance + speed).toLong()) // default. DO NOT TOUCH!
            literals.delayForFirstAppearance += literals.timeCardIsClose // time the card is being opened
            finalBut.postDelayed({
                finalBut.text = "" // closed
                finalBut.background.setColorFilter(
                    getColorOfButtons(getIndex(finalBut.id)),
                    PorterDuff.Mode.MULTIPLY
                )
            }, literals.delayForFirstAppearance.toLong()) // default. DO NOT TOUCH!
            literals.delayForFirstAppearance += literals.timeCardIsOpen // time between closed and next opened card
        }
        if (SharedPreferencesUtil.getComplexity(this) == 2) circleCards()
    }

    fun pressedButton(index: Int): Button {
        return cards[index]
    }

    fun getIndex(index: Int): Int {
        return index - convertIdToIndex
    }

    fun updateViewFromModel() {
        for (index in 0 until numberOfCards) {
            val card = gameLogic!!.cards[index]
            if (card != null) {
                functionForPressedButton(cards[index], card)
            }
        }
    }

    fun functionForPressedButton(button: Button, card: Card) {
        button.textSize = 43f
        if (card.isFaceUp) {
            button.text = getEmoji(card)
            button.background.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        } else {
            button.text = ""
            if (!card.isMatched) {
                button.background.setColorFilter(
                    getColorOfButtons(getIndex(button.id)),
                    PorterDuff.Mode.MULTIPLY
                )
            } else {
                button.background.setColorFilter(
                    resources.getColor(R.color.no_color),
                    PorterDuff.Mode.MULTIPLY
                )
                button.isEnabled = false
            }
        }
    }

    private val emoji: MutableMap<Int, String> = HashMap()
    private val animals = arrayOf("🐶", "🐱", "🐼", "🦊", "🦁", "🐯", "🐨", "🐮", "🐷", "🐵")
    private val cars = arrayOf("🚔", "🏎", "🚕", "🚚", "🚜", "🚛", "🚑", "🚎", "🚙", "🚒")
    private val food = arrayOf("🍇", "🍌", "🍔", "🎂", "🌽", "🍉", "🍎", "🥕", "🌶", "🍕")
    private val random = arrayOf(
        "🏰",
        "🐨",
        "🐝",
        "🦂",
        "🦖",
        "⛄️",
        "🛸",
        "💻",
        "🏁",
        "💂",
        "💍",
        "🐒",
        "🐊",
        "🎄",
        "🏍",
        "👾",
        "🦁",
        "🐿",
        "🔥",
        "🌘",
        "🍕",
        "⚽️",
        "🥁",
        "🧀",
        "🛩",
        "📸",
        "🎁",
        "🍏",
        "🐩",
        "🐓",
        "🍁",
        "🌈",
        "🦈",
        "🛏",
        "📚",
        "🗿",
        "🎭",
        "🍿",
        "🥥",
        "🍆",
        "🦔",
        "🎮️",
        "🌶",
        "🐘",
        "🚔",
        "🎡",
        "🏔",
        "🚄",
        "🎬",
        "🐙",
        "🍄",
        "🌵",
        "🐢",
        "👑",
        "🧞",
        "👻",
        "🕶",
        "🎓",
        "🎪",
        "🐶",
        "🐲",
        "🍓",
        "🏆",
        "🎰"
    )
    private val themes = arrayOf(
        Theme(animals),
        Theme(cars),
        Theme(food),
        Theme(random)
    )
    private var cutEmoji = 0
    fun getEmoji(card: Card): String? {
        val emojiTypes = themes[SharedPreferencesUtil.getTheme(this)].emojis
        if (emoji[card.identifier] == null && emojiTypes.size > 0) {
            val randomIndex = (Math.random() * (emojiTypes.size - cutEmoji)).toInt()
            emoji[card.identifier] = emojiTypes[randomIndex]
            if (emojiTypes.size - 1 - randomIndex >= 0) System.arraycopy(
                emojiTypes,
                randomIndex + 1,
                emojiTypes,
                randomIndex,
                emojiTypes.size - 1 - randomIndex
            )
            cutEmoji++
        }
        return if (emoji[card.identifier] != null) emoji[card.identifier] else "?"
    }
}