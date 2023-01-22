package com.example.places

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddPlacesActivity : AppCompatActivity(){
    private val calendar:Calendar = Calendar.getInstance()

    private lateinit var dateSetListener: OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_places)
        val toolbarAddPlaces = findViewById<Toolbar>(R.id.toolbar_add_places)
        setSupportActionBar(toolbarAddPlaces)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarAddPlaces.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar?.set(Calendar.YEAR, year)
            calendar?.set(Calendar.MONTH, month)
            calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        val etDate = findViewById<AppCompatEditText>(R.id.et_date)
        etDate.setOnClickListener { onSelectDate() }

    }

    private fun updateDateInView() {
        val etDate = findViewById<AppCompatEditText>(R.id.et_date)
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        etDate.setText(sdf.format(calendar.time).toString())
    }


    private fun onSelectDate() {
            DatePickerDialog(this,
                dateSetListener,
                calendar.get(Calendar.YEAR) ,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
    }
}