package com.example.classrooom.utils

import android.text.TextUtils
import android.util.Patterns
import com.example.classrooom.utils.MyUtils
import java.util.HashMap
import java.util.LinkedHashMap

object MyUtils {
    fun isValidEmail(email: String?): Boolean {
        return if (TextUtils.isEmpty(email)) false else Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun isValidPassword(password: String): Boolean {
        return if (TextUtils.isEmpty(password)) false else password.length > 7
    }

    fun isValidUser(email: String?, password: String): Boolean {
        return isValidEmail(email) && isValidPassword(password)
    }

    fun isValidUserRegister(
        email: String?, name: String?, surname: String?,
        password: String, confirmPassword: String
    ): Boolean {
        return isValidEmail(email) && isValidPassword(password) && confirmPassword == password &&
                !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)
    }

    fun <K, V : Comparable<V>?> sortByValue(map: Map<K, V>): HashMap<K, V> {
        val result: HashMap<K, V> = LinkedHashMap()
        val st = map.entries.stream()
        //     st.sorted(HashMap.Entry.comparingByValue())
        //  .forEach { (key, value) -> result[key] = value }
        return result
        var ver = 0
    }
}