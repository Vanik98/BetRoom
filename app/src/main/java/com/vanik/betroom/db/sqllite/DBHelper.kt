package com.vanik.betroom.db.sqllite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import com.vanik.betroom.db.room.AppDatabase

class DBHelper private constructor(context: Context) : SQLiteOpenHelper(context,"database_lite",null,1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table Actor ("
                + "name text primary key NOT NULL,"
                + "surname text NOT NULL,"
                + "age integer NOT NULL,"
                + "pets text,"
                + "movieIds text" + ");")

        db.execSQL("create table Movie ("
                + "id integer primary key NOT NULL,"
                + "name text NOT NULL,"
                + "imdbRate real NOT NULL" + ");")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getInstance(context: Context): DBHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DBHelper(context).also { INSTANCE = it }
            }

    }
}