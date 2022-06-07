package com.example.classrooom.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.example.classrooom.data.DataBaseHelper

class DataBaseHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    private val COLUMN_ID = "id"
    private val COLUMN_NAME = "Name"
    private val COLUMN_PERCENTS = "Percents"
    private val COLUMN_FLIPS = "Flips"
    private val COLUMN_POINTS = "Points"
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT UNIQUE, "
                + COLUMN_PERCENTS + " REAL, "
                + COLUMN_FLIPS + " INTEGER, "
                + COLUMN_POINTS + " INTEGER);")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val TABLE_NAME = "GameRes"
    }
}