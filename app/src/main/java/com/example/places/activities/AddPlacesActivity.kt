package com.example.places.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.example.places.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddPlacesActivity : AppCompatActivity(){

    private val calendar:Calendar = Calendar.getInstance()

    private lateinit var dateSetListener: OnDateSetListener

    private var galleryResultLauncher: ActivityResultLauncher<Intent>? = null

    private var cameraResultLauncher: ActivityResultLauncher<Intent>? = null

    private var externalImagePath = ""
    private var latitude = 0.0
    private var longitude = 0.0

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
        onClick<Button>(R.id.btn_save){ onSaveImage() }

        galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handleGalleryActivityResult(it)
        }

        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handleCameraActivityResult(it)
        }

    }

    private fun onSaveImage() {

    }

    private fun handleCameraActivityResult(result: ActivityResult?) {
        if (result!!.resultCode == Activity.RESULT_OK) {
            val thumbnail = result.data!!.extras!!.get("data") as Bitmap
            externalImagePath = saveImageToInternalStorage(thumbnail).toString()
            Log.i("CAMERA", externalImagePath.toString())
            findViewById<AppCompatImageView>(R.id.iv_place_image).setImageBitmap(thumbnail)
        }
    }

    private fun handleGalleryActivityResult(result: ActivityResult?) {
        if (result!!.resultCode == Activity.RESULT_OK) {
            val contentUri = result.data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
            externalImagePath = saveImageToInternalStorage(bitmap).toString()
            Log.i("GALLERY", externalImagePath)
            findViewById<AppCompatImageView>(R.id.iv_place_image).setImageBitmap(bitmap)
        }
    }

    private fun onAddImage() {
        AlertDialog.Builder(this)
            .setTitle("Specify Image Source")
            .setItems(arrayOf("Gallery", "Camera")){ dlg: DialogInterface, which: Int ->
                when(which) {
                    0 -> onSelectFromGallery()
                    1 -> onSelectFromCamera()
                }
            }
            .show()
    }

    private fun onSelectFromCamera() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        openCamera()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check();
    }



    private fun onSelectFromGallery() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        openGallery()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check();
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher!!.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher!!.launch(galleryIntent)
    }

    private fun showRationaleDialogForPermissions() {
        AlertDialog.Builder(this)
            .setTitle("Permissions")
            .setMessage("Seems you have denied permissions required by the app. You can enable the permissions from settings.")
            .setPositiveButton("Go to settings") { dlg, _ ->
                dlg.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dlg, _ ->
                dlg.dismiss()
            }
            .setCancelable(false)
            .show()

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

    private fun saveImageToInternalStorage(image: Bitmap): Uri {
        val ctxWrapper = ContextWrapper(applicationContext)
        val dir = ctxWrapper.getDir(PLACES_IMAGES, Context.MODE_PRIVATE)
        val file = File(dir, "${UUID.randomUUID()}.jpeg")
        try {
            val fsStream  = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fsStream)
            fsStream.flush()
            fsStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val PLACES_IMAGES = "PlacesImages"
    }

}


