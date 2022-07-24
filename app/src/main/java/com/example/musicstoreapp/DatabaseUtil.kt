package com.example.musicstoreapp

import android.content.ContentValues
import android.content.Context

class DatabaseUtil(context: Context?) {
    private val dbHelper = DatabaseHelper(context)

    // sqlite has no boolean datatype, so I use 1 as true and 0 as false for parameter favourite
    fun markMusicStoreAsFavourite(withId: Int, favourite: Int): Int {
        val db = dbHelper.writableDatabase

        val valuesToUpdate = ContentValues().apply {
            put("favourite", favourite)
        }

        val result = db.update(
            DatabaseHelper.TABLE_NAME,
            valuesToUpdate,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(withId.toString())
        )

        db.close()

        return result
    }
}