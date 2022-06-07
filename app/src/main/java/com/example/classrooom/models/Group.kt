package com.example.classrooom.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Group {
    @JvmField
    var inviteCode: String? = null
    @JvmField
    var creatorId: String? = null
    @JvmField
    var groupName: String? = null
    private fun createInviteCode(len: Int): String {
        val leftLimit = 48 // numeral '0'
        val rightLimit = 122 // letter 'z'
        val random = Random()
        return random.ints(leftLimit, rightLimit + 1)
            .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
            .limit(len.toLong())
            .collect(
                { StringBuilder() },
                { obj: java.lang.StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: java.lang.StringBuilder, s: java.lang.StringBuilder? ->
                obj.append(
                    s
                )
            }
            .toString()
    }

    internal constructor() {}
    constructor(creatorId: String?, groupName: String?) {
        inviteCode = createInviteCode(5)
        this.creatorId = creatorId
        this.groupName = groupName
    }
}