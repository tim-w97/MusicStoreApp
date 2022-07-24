package com.example.musicstoreapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.lang.StringBuilder

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_FILENAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_FILENAME = "music_store.db"

        const val TABLE_NAME = "stores"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_CITY = "city"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_LAT = "lat"
        const val COLUMN_LON = "lon"
        const val COLUMN_FAVOURITE = "favourite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = StringBuilder()
            .append("CREATE TABLE $TABLE_NAME (")
            .append("$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append("$COLUMN_NAME TEXT, ")
            .append("$COLUMN_CITY TEXT, ")
            .append("$COLUMN_DESCRIPTION TEXT, ")
            .append("$COLUMN_LAT REAL, ")
            .append("$COLUMN_LON REAL, ")
            .append("$COLUMN_FAVOURITE INT DEFAULT 0)")
            .toString()

        db?.execSQL(createTableStatement)

        val stores = arrayOf(
            ContentValues().apply {
                put(COLUMN_NAME, "JazzWerk")
                put(COLUMN_CITY, "Hof")
                put(COLUMN_DESCRIPTION, "Alles, was ihr Jazz-Herz begehrt.")
                put(COLUMN_LAT, 50.307166)
                put(COLUMN_LON, 11.911236)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "Just Music GmbH")
                put(COLUMN_CITY, "Berlin")
                put(
                    COLUMN_DESCRIPTION,
                    "Ein Berliner ohne Musikinstrument ist nur ein halber Berliner."
                )
                put(COLUMN_LAT, 52.5039322)
                put(COLUMN_LON, 13.4066882)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "J&M Musikland")
                put(COLUMN_CITY, "Erfurt")
                put(
                    COLUMN_DESCRIPTION,
                    "Tim hat hier schonmal eine Gitarre gekauft. Der Laden kann nur gut sein."
                )
                put(COLUMN_LAT, 50.9726803)
                put(COLUMN_LON, 11.0281189)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "PiccoBello Musikladen")
                put(COLUMN_CITY, "Regensburg")
                put(
                    COLUMN_DESCRIPTION,
                    "Die Auswahl ist etwas klein, aber die Beratung dafür sehr gut."
                )
                put(COLUMN_LAT, 49.019262)
                put(COLUMN_LON, 12.100752)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "Musikhaus Syhre")
                put(COLUMN_CITY, "Leipzig")
                put(
                    COLUMN_DESCRIPTION,
                    "Verkäufer nimmt sich Zeit für persönliche Beratung, niedrige Preise im Vergleich"
                )
                put(COLUMN_LAT, 51.3658413)
                put(COLUMN_LON, 12.3565743)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "Musik Markt Buxtehude")
                put(COLUMN_CITY, "Buxtehude")
                put(
                    COLUMN_DESCRIPTION,
                    "Ich habe selbst 2 Gitarren dort gekauft und einen Verstärker und bin mega zufrieden damit."
                )
                put(COLUMN_LAT, 53.4785309)
                put(COLUMN_LON, 9.699392)
            },
            ContentValues().apply {
                put(COLUMN_NAME, "Mr. Music")
                put(COLUMN_CITY, "Jena")
                put(
                    COLUMN_DESCRIPTION,
                    "Man findet jedes Mal eine Platte die man mitnehmen will."
                )
                put(COLUMN_LAT, 51.0968057)
                put(COLUMN_LON, 5.9675996)
            },
        )

        for (store in stores) {
            db?.insert(TABLE_NAME, null, store)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropStatement = "DROP TABLE $TABLE_NAME"

        db?.execSQL(dropStatement)

        onCreate(db)
    }

}