package com.example.places.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.adapters.OnClickListener
import com.example.places.adapters.OnLongClickListener
import com.example.places.adapters.PlacesAdapter
import com.example.places.database.DatabaseHandler
import com.example.places.databinding.ActivityMainBinding
import com.example.places.models.Place

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handleActivityResult(it)
        }

        binding?.fabAddPlaces?.setOnClickListener{
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
            binding?.rvPlacesList?.visibility = View.GONE
            binding?.tvRecordNotAvailable?.visibility = View.VISIBLE
        }

    }

    private fun setupRecyclerView(places: List<Place>) {
        val adapter = PlacesAdapter(this, places)
        adapter.setOnCLickListener(object : OnClickListener{
            override fun onClick(position: Int, model: Place) {
                val intent = Intent(this@MainActivity, ViewPlaceActivity::class.java)
                intent.putExtra("place_detail", model)
                startActivity(intent)
            }
        })

        adapter.setOnLongCLickListener(object: OnLongClickListener{
            override fun onLongClick(position: Int, model: Place): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Action")
                    .setItems(arrayOf("Edit", "Delete")){ _: DialogInterface, which: Int ->
                        when(which) {
                            0 -> editPlace(model)
                            1 -> deletePlace(model)
                        }
                    }
                    .show()
                return true
            }

        })
        binding?.rvPlacesList?.visibility = View.VISIBLE
        binding?.rvPlacesList?.layoutManager = LinearLayoutManager(this)
        binding?.rvPlacesList?.setHasFixedSize(true)
        binding?.rvPlacesList?.adapter = adapter
        binding?.tvRecordNotAvailable?.visibility = View.GONE
    }

    private fun deletePlace(model: Place) {
        val dbh = DatabaseHandler(this)
        dbh.delete(model)
        getPlacesFromDB()
    }

    private fun editPlace(model: Place) {
        val intent = Intent(this@MainActivity, AddPlacesActivity::class.java)
        intent.putExtra("place_detail", model)
        resultLauncher!!.launch(intent)
    }
}