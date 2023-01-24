package com.example.places.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.places.R
import com.example.places.databinding.ActivityViewPlaceBinding
import com.example.places.models.Place

class ViewPlaceActivity : AppCompatActivity() {
    private var binding: ActivityViewPlaceBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarViewPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarViewPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra("place_detail")) {
            val place = intent.extras?.get("place_detail") as Place
            binding?.ivImage?.setImageURI(Uri.parse(place.image))
            binding?.tvDescription?.text = place.description
            binding?.tvLocation?.text = place.location
            supportActionBar?.title = place.title
        }
    }
}