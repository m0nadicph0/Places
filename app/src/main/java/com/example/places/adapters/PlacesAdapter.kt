package com.example.places.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.places.R
import com.example.places.models.Place
import de.hdodenhof.circleimageview.CircleImageView

class PlacesAdapter(private val context: Context, private val dataset: List<Place>): RecyclerView.Adapter<PlacesHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesHolder {
        return PlacesHolder(LayoutInflater.from(context).inflate(R.layout.item_place, parent, false))
    }

    override fun onBindViewHolder(holder: PlacesHolder, position: Int) {
        val model = dataset[position]
        holder.itemView.findViewById<CircleImageView>(R.id.civ_place).setImageURI(Uri.parse(model.image))
        holder.itemView.findViewById<TextView>(R.id.tv_title).text = model.title
        holder.itemView.findViewById<TextView>(R.id.tv_description).text = model.description
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position, model)
        }
        holder.itemView.setOnLongClickListener{
            onLongClickListener?.onLongClick(position, model)!!
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setOnCLickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    fun setOnLongCLickListener(listener: OnLongClickListener) {
        onLongClickListener = listener
    }
}