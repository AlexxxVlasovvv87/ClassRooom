package com.example.classrooom.data

import android.content.Context
import com.example.classrooom.data.SharedPreferencesUtil
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharedPreferencesUtil {
    fun saveUserLevel(context: Context, level: Int) {
        saveValue(context, level.toString(), "level")
    }

    fun getUserLevel(context: Context): Int {
        var mDatabase = getValue(context, "level")
        if (mDatabase == null) mDatabase = "1"
        return mDatabase.toInt()
    }

    fun saveComplexity(context: Context, colors: Int) {
        saveValue(context, colors.toString(), "difLvl")
    }

    fun getComplexity(context: Context): Int {
        var mDatabase = getValue(context, "difLvl")
        if (mDatabase == null) mDatabase = "0"
        return mDatabase.toInt()
    }

    fun saveTheme(context: Context, theme: Int) {
        saveValue(context, theme.toString(), "theme")
    }

    fun getTheme(context: Context): Int {
        var mDatabase = getValue(context, "theme")
        if (mDatabase == null) mDatabase = "0"
        return mDatabase.toInt()
    }

    private fun saveValue(context: Context, data: String, key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, data)
        editor.apply()
    }

    private fun getValue(context: Context, key: String): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, null)
    }
}