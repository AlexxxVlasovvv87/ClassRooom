package com.example.classrooom.info

import com.example.classrooom.info.Variables

class Variables {
    fun setChange(fl: Boolean): Int {
        return getUniqueChange(fl)
    }

    companion object {
        private var change1 = 50
        private var change2 = 60
        private var res = 0
        private fun getUniqueChange(fl: Boolean): Int {
            if (fl) {
                if (change1 <= 60) {
                    res += change1
                    change1 += 10
                } else {
                    res = if (change2 == 60) Math.round(
                        Math.random() * 20 - 160
                    )
                        .toInt() else res - change2
                    change2 += 60
                }
            } else {
                res = 50
                change1 = 60
                change2 = 60
            }
            return res
        }
    }
}