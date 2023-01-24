package com.example.places.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.places.R
import com.example.places.databinding.ActivityViewPlaceBinding

class ViewPlaceActivity : AppCompatActivity() {
    private var binding: ActivityViewPlaceBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}