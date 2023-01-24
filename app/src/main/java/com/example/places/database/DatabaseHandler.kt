package com.example.places.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.places.models.Place


class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DBNAME, null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {

        val query = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COl INTEGER PRIMARY KEY, " +
                "$TITLE_COl TEXT, " +
                "$IMAGE_COL TEXT, " +
                "$DESCRIPTION_COl TEXT, " +
                "$DATE_COL TEXT, " +
                "$LOCATION_COl TEXT, " +
                "$LATITUDE_COL DOUBLE, " +
                "$LONGITUDE_COL DOUBLE)"

        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPlace(place: Place):Long {
        val values = ContentValues()
        values.put(TITLE_COl, place.title)
        values.put(IMAGE_COL, place.image)
        values.put(DESCRIPTION_COl, place.description)
        values.put(DATE_COL, place.date)
        values.put(LOCATION_COl, place.location)
        values.put(LATITUDE_COL, place.latitude)
        values.put(LONGITUDE_COL, place.longitude)

        val db = this.writableDatabase
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun fetchAll(): List<Place> {
        val result = ArrayList<Place>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if(cursor!!.moveToFirst()) {
            do {
                result.add(
                    Place(
                        getIntValueForCol(cursor, ID_COl),
                        getStringValueForCol(cursor, TITLE_COl),
                        getStringValueForCol(cursor, IMAGE_COL),
                        getStringValueForCol(cursor, DESCRIPTION_COl),
                        getStringValueForCol(cursor, DATE_COL),
                        getStringValueForCol(cursor, LOCATION_COl),
                        getDoubleValueForCol(cursor, LATITUDE_COL),
                        getDoubleValueForCol(cursor, LONGITUDE_COL)
                    )
                )
            } while(cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    private fun getStringValueForCol(cursor: Cursor, name: String): String {
        val columnIndex = cursor.getColumnIndex(name)
        return if(columnIndex != -1) {
            cursor.getString(columnIndex)
        } else {
            ""
        }

    }

    private fun getIntValueForCol(cursor: Cursor, name: String): Int {
        val columnIndex = cursor.getColumnIndex(name)
        return if(columnIndex != -1) {
            cursor.getInt(columnIndex)
        } else {
            0
        }

    }

    private fun getDoubleValueForCol(cursor: Cursor, name: String): Double {
        val columnIndex = cursor.getColumnIndex(name)
        return if(columnIndex != -1) {
            cursor.getDouble(columnIndex)
        } else {
            0.0
        }
    }

    fun updatePlace(place: Place):Long {
        val values = ContentValues()
        values.put(TITLE_COl, place.title)
        values.put(IMAGE_COL, place.image)
        values.put(DESCRIPTION_COl, place.description)
        values.put(DATE_COL, place.date)
        values.put(LOCATION_COl, place.location)
        values.put(LATITUDE_COL, place.latitude)
        values.put(LONGITUDE_COL, place.longitude)

        val db = this.writableDatabase
        val clause = "id=${place.id}"
        val result = db.update(TABLE_NAME, values, clause, null)
        db.close()
        return result.toLong()
    }

    companion object {
        private const val DBNAME = "places_db"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "places"

        private const val ID_COl = "id"
        private const val TITLE_COl = "title"
        private const val IMAGE_COL = "image"
        private const val DESCRIPTION_COl = "description"
        private const val DATE_COL = "date"
        private const val LOCATION_COl = "location"
        private const val LATITUDE_COL = "latitude"
        private const val LONGITUDE_COL = "longitude"
    }

}