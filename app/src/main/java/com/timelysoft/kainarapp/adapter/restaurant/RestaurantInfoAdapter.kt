package com.timelysoft.kainarapp.adapter.restaurant

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.loadImage
import kotlinx.android.synthetic.main.item_image_rv.view.*

class RestaurantInfoAdapter(list: ArrayList<String>) :
    GenericRecyclerAdapter<String>(list){

    override fun bind(item: String, holder: ViewHolder) {
        holder.itemView.restaurant_image_picture.loadImage(item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_image_rv)
    }

}