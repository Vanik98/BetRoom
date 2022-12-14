package com.vanik.growdb.sqllite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context) : SQLiteOpenHelper(context,"database_lite",null,1) {
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
                + "imdbRate real NOT NULL,"
                + "actorName text NOT NULL,"
                + "FOREIGN KEY (actorName) REFERENCES Actor(name)"
                + ");")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}