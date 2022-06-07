package com.example.classrooom.models

class Card {
    var isFaceUp = false
    var isMatched = false
    var identifier: Int

    companion object {
        private var idCreator = 0
        private val uniqueIdentifier: Int
            private get() {
                idCreator++
                return idCreator
            }
    }

    init {
        identifier = uniqueIdentifier
    }
}