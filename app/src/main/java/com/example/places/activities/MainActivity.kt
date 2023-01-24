package com.example.places.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.places.R
import com.example.places.adapters.PlacesAdapter
import com.example.places.database.DatabaseHandler
import com.example.places.models.Place
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handleActivityResult(it)
        }

        findViewById<FloatingActionButton>(R.id.fab_add_places).setOnClickListener{
            val intent = Intent(this, AddPlacesActivity::class.java)
            resultLauncher!!.launch(intent)
        }

        getPlacesFromDB()
    }

    private fun handleActivityResult(result: ActivityResult?) {
        if (result!!.resultCode == Activity.RESULT_OK) {
            getPlacesFromDB()
        }
    }

    private fun getPlacesFromDB() {
        val dbh = DatabaseHandler(this)
        val places = dbh.fetchAll()

        if (places.isNotEmpty()) {
            setupRecyclerView(places)
        } else {
            findViewById<RecyclerView>(R.id.rv_places_list).visibility = View.GONE
            findViewById<TextView>(R.id.tv_record_not_available).visibility = View.VISIBLE
        }

    }

    private fun setupRecyclerView(places: List<Place>) {
        val adapter = PlacesAdapter(this, places)
        val rvPlaces = findViewById<RecyclerView>(R.id.rv_places_list)
        rvPlaces.visibility = View.VISIBLE
        rvPlaces.layoutManager = LinearLayoutManager(this)
        rvPlaces.setHasFixedSize(true)
        rvPlaces.adapter = adapter
        findViewById<TextView>(R.id.tv_record_not_available).visibility = View.GONE
    }
}