package com.example.classrooom.info

import com.google.firebase.database.IgnoreExtraProperties
import com.example.classrooom.info.Post
import com.google.firebase.database.Exclude
import java.util.HashMap

@IgnoreExtraProperties
class Post : Comparable<Post> {
    var uid: String? = null
    var username: String? = null
    var percents: String? = null

    constructor() {}
    constructor(uid: String?, username: String?, percents: String?) {
        this.uid = uid
        this.username = username
        this.percents = percents
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["uid"] = uid
        result["username"] = username
        result["percents"] = percents
        return result
    }

    override fun compareTo(o: Post): Int {
        if (percents == null || o.percents == null) {
            return 0
        }
        val first = percents!!.toDouble()
        val second = o.percents!!.toDouble()
        return second.compareTo(first)
    }
}