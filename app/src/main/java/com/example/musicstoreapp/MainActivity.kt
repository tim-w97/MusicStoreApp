package com.example.musicstoreapp

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var musicStoreList: ListView
    private lateinit var db: SQLiteDatabase
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicStoreList = findViewById(R.id.musicStoreList)
        db = DatabaseHelper(this).readableDatabase

        initTextWatcher()
        buildList()

        toast = Toast.makeText(
            this,
            "Es wurde kein Musikladen zu dieser Stadt gefunden.",
            Toast.LENGTH_SHORT
        )

        musicStoreList.setOnItemClickListener { parent, view, position, id ->
            val cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                arrayOf(
                    DatabaseHelper.COLUMN_ID,
                    DatabaseHelper.COLUMN_NAME,
                    DatabaseHelper.COLUMN_CITY,
                    DatabaseHelper.COLUMN_DESCRIPTION,
                    DatabaseHelper.COLUMN_LAT,
                    DatabaseHelper.COLUMN_LON,
                    DatabaseHelper.COLUMN_FAVOURITE
                ),
                "${DatabaseHelper.COLUMN_ID} = ?",
                arrayOf(id.toString()),
                null, null, null
            )

            if (cursor.moveToFirst()) {
                Intent(this, MusicStoreDetailsView::class.java).apply {
                    putExtra(
                        "name",
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
                    )
                    putExtra(
                        "city",
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY))
                    )
                    putExtra(
                        "description",
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))
                    )
                    putExtra(
                        "lat",
                        cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAT))
                    )

                    putExtra(
                        "lon",
                        cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_LON))
                    )

                    view.animate().rotation(360f).withEndAction {
                        startActivity(this)
                    }
                }
            }
            cursor.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutThisApp -> {
                Intent(this, AboutMeActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.requestCameraPermission -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA), 0
                )
            }

            R.id.showNotificationWithCurrentTime -> {
                val channelName = "the_coolest_notification_channel_in_the_world"
                val channelId = "my_channel_id"
                val notificationId = 42

                //create notification channel and register it
                val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.createNotificationChannel(notificationChannel)

                // build the notification
                val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(com.google.android.material.R.drawable.ic_clock_black_24dp)
                    .setContentTitle(getString(R.string.notification_text))
                    .setContentText(getCurrentClockTime())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // show the notification with notify() call
                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, notificationBuilder.build())
                }
            }

            R.id.showExampleAlert -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_title))
                builder.setMessage(getString(R.string.dialog_text))

                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                    // nothing happens here
                }

                builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                    // nothing happens here too
                }

                builder.setNeutralButton(R.string.neutral_button_text) { dialog, which ->
                    // and here too
                }

                builder.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    private fun getCurrentClockTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        return "$formatted Uhr"
    }

    private fun initTextWatcher() {
        val citySearch = findViewById<EditText>(R.id.citySearch)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(editable: Editable?) {
                adjustList(editable.toString())
            }
        }

        citySearch.addTextChangedListener(textWatcher)
    }

    private fun buildList() {
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_CITY,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_LAT,
                DatabaseHelper.COLUMN_LON,
                DatabaseHelper.COLUMN_FAVOURITE,
            ),
            null, null, null, null, null
        )

        musicStoreList.adapter = MyCursorAdapter(this, cursor)
    }

    private fun adjustList(byCity: String) {
        val noMusicStoresFoundText = findViewById<TextView>(R.id.noMusicStoresFoundText)
        noMusicStoresFoundText.visibility = TextView.INVISIBLE

        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_CITY
            ),
            "${DatabaseHelper.COLUMN_CITY} LIKE ?",
            arrayOf("$byCity%"), null, null, null
        )

        val adapter = musicStoreList.adapter as MyCursorAdapter
        adapter.changeCursor(cursor)

        // if the cursor got changed and there is no data, show a simple Toast message
        if (cursor.count == 0) {
            noMusicStoresFoundText.visibility = TextView.VISIBLE
            toast.show()
        }
    }
}