package com.example.musicstoreapp

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CursorAdapter
import android.widget.Switch
import android.widget.TextView

class MyCursorAdapter(context: Context?, cursor: Cursor?) : CursorAdapter(context, cursor, true) {
    // data holder for the music stores
    data class MusicStore(
        val name: TextView,
        val city: TextView,
        val mapsButton: Button,
        val favouriteSwitch: Switch
    )

    override fun newView(context: Context?, p1: Cursor?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)

        return inflater.inflate(R.layout.store_item, parent, false).apply {
            tag = MusicStore(
                findViewById(R.id.storeName),
                findViewById(R.id.cityName),
                findViewById(R.id.mapsButton),
                findViewById(R.id.favouriteSwitch)
            )
        }
    }

    override fun bindView(rowView: View?, context: Context?, cursor: Cursor?) {
        val holder = rowView?.tag as MusicStore

        holder.city.text = cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY))
        holder.name.text = cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))

        val favourite = cursor?.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAVOURITE))
        holder.favouriteSwitch.isChecked = favourite == 1
        holder.favouriteSwitch.tag = cursor?.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))

        holder.mapsButton.setOnClickListener {
            val lat = cursor?.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAT))
            val lon = cursor?.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_LON))

            // ?q=$lat,$lon(Ort) is for showing a location marker named "Ort" at the position
            val uriString = "geo:$lat,$lon?q=$lat,$lon(Ort)"

            Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                context?.startActivity(this)
            }
        }

        holder.favouriteSwitch.setOnClickListener {
            val switch = it as Switch
            val id = switch.tag as Int

            DatabaseUtil(context).markMusicStoreAsFavourite(
                id,
                if (switch.isChecked) 1 else 0
            )
        }
    }
}