package com.example.places.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.places.R
import com.example.places.database.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<FloatingActionButton>(R.id.fab_add_places).setOnClickListener{
            val intent = Intent(this, AddPlacesActivity::class.java)
            startActivity(intent)
        }

        getPlacesFromDB()
    }

    private fun getPlacesFromDB() {
        val dbh = DatabaseHandler(this)
        val places = dbh.fetchAll()

        places.forEach {
            Log.i("DATA", "${it.title}")
        }
    }
}