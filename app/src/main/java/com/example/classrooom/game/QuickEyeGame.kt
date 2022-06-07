package com.example.classrooom.game

import com.example.classrooom.info.Literals
import com.example.classrooom.models.Card
import java.util.*

class QuickEyeGame(numberOfPairsOfCards: Int) {
    var cards = ArrayList<Card?>()
    private val openedCards = ArrayList<Int>()
    private var idOfFaceUpCard = -1
    var mistakePoints = 0
    fun chooseCard(index: Int) {
        if (!cards[index]!!.isMatched) {
            val matchIndex = idOfFaceUpCard
            if (matchIndex != -1 && matchIndex != index) {
                if (cards[matchIndex]!!.identifier == cards[index]!!.identifier) {
                    cards[matchIndex]!!.isMatched = true
                    cards[index]!!.isMatched = true
                    mistakePoints += Literals.match
                } else {
                    if (openedCards.contains(index)) {
                        mistakePoints -= Literals.miss
                    }
                    if (openedCards.contains(matchIndex)) {
                        mistakePoints -= Literals.miss
                    }
                    openedCards.add(index)
                    openedCards.add(matchIndex)
                }
                cards[index]!!.isFaceUp = true
                idOfFaceUpCard = -1
            } else {
                for (value in cards) value!!.isFaceUp = false
                cards[index]!!.isFaceUp = true
                idOfFaceUpCard = index
            }
        }
    }

    fun checkForAllMatchedCards(): Boolean {
        var result = false
        for (i in cards.indices) {
            if (!cards[i]!!.isMatched) {
                result = true
                break
            }
        }
        return !result
    }

    init {
        for (i in 0 until numberOfPairsOfCards * 2) {
            val card = Card()
            cards.add(card)
        }
        var j = numberOfPairsOfCards * 2 - 1
        while (j >= 0) {
            cards[j]!!.identifier = cards[j - 1]!!.identifier
            j -= 2
        }
        Collections.shuffle(cards)
    }
}