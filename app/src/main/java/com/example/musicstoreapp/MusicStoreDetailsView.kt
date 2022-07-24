package com.example.musicstoreapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MusicStoreDetailsView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_store_details_view)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val location = intent.getStringExtra("location")

        val lat = intent.getFloatExtra("lat", 0f)
        val lon = intent.getFloatExtra("lon", 0f)

        findViewById<TextView>(R.id.musicStoreTitle).text = name
        findViewById<TextView>(R.id.musicStoreDescription).text = description
        findViewById<TextView>(R.id.musicStoreLocation).text = location

        findViewById<Button>(R.id.showGoogleMapsButton).setOnClickListener {
            // ?q=$lat,$lon(Ort) is for showing a location marker named "Ort" at the position
            val uriString = "geo:$lat,$lon?q=$lat,$lon(Ort)"

            Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
}