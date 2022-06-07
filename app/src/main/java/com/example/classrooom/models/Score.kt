package com.example.classrooom.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Score {
    @JvmField
    var progress = 0
    @JvmField
    var points = 0

    constructor() {}
    constructor(progress: Int, points: Int) {
        this.progress = progress
        this.points = points
    }
}