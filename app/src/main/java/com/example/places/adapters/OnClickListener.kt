package com.example.places.adapters

import com.example.places.models.Place

interface OnClickListener {
    fun onClick(position: Int, model: Place)
}