package com.example.classrooom.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {
    @JvmField
    var uid: String? = null
    @JvmField
    var name: String? = null
    @JvmField
    var surname: String? = null
    @JvmField
    var email: String? = null
    @JvmField
    var isTeacher = false

    constructor() {}
    constructor(email: String?, name: String?, surname: String?) {
        uid = "-1"
        this.email = email
        this.name = name
        this.surname = surname
        isTeacher = false
    }

    constructor(email: String?, name: String?, surname: String?, isTeacher: Boolean) {
        uid = "-1"
        this.email = email
        this.name = name
        this.surname = surname
        this.isTeacher = isTeacher
    }

    constructor(uid: String?, email: String?, name: String?, surname: String?, isTeacher: Boolean) {
        this.uid = uid
        this.email = email
        this.name = name
        this.surname = surname
        this.isTeacher = isTeacher
    }
}