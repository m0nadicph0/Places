package com.example.places

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
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

        dateSetListener = OnDateSetListener { view, year, month, dayOfMonth ->
            calendar?.set(Calendar.YEAR, year)
            calendar?.set(Calendar.MONTH, month)
            calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        onClick<AppCompatEditText>(R.id.et_date){ onSelectDate() }
        onClick<Button>(R.id.btn_add_image) { onAddImage() }

    }

    private fun onAddImage() {
        AlertDialog.Builder(this)
            .setTitle("Specify Image Source")
            .setItems(arrayOf("Gallery", "Camera")){ dlg: DialogInterface, which: Int ->
                when(which) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePhotoFromCamera()
                }
            }
            .show()
    }

    private fun takePhotoFromCamera() {
        TODO("Not yet implemented")
    }

    private fun choosePhotoFromGallery() {
        TODO("Not yet implemented")
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

    private fun <T : View?> onClick(resId: Int, handler: (View?) -> Unit) {
        findViewById<T>(resId)?.setOnClickListener(handler)
    }

    private fun lToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}