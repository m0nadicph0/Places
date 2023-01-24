package com.example.places.adapters

import com.example.places.models.Place

interface OnLongClickListener {
    fun onLongClick(position: Int, model: Place):Boolean
}