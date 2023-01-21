package com.example.places

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<FloatingActionButton>(R.id.fab_add_places).setOnClickListener{
            val intent = Intent(this, AddPlacesActivity::class.java)
            startActivity(intent)
        }
    }
}