package com.example.classrooom.info

import com.example.classrooom.info.Literals

class Literals {
    var delayForFirstAppearance = 350
    var delayBetweenAppearance = 80
    var timeCardIsOpen = 300
    var timeCardIsClose = 250

    companion object {
        const val maxLevel: Short = 5
        private var levelNumber = 0
        private var numberOfBut = 0
        const val match = 2
        const val miss = 1
        var points = 0
        val maximumPoints: Double
            get() {
                var res = 0
                var c = 4
                for (i in 0 until maxLevel) {
                    res += c * 2
                    c += 4
                }
                return res.toDouble()
            }

        fun getNumberOFButtons(flag: Boolean): Int {
            if (flag) numberOfBut += 4 else numberOfBut = 4
            return numberOfBut
        }

        fun getLevelNumber(flag: Boolean): Int {
            if (flag) levelNumber++ else levelNumber = 1
            return levelNumber
        }
    }
}