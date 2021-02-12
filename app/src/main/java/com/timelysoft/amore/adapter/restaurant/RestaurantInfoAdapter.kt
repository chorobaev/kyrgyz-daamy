package com.timelysoft.amore.adapter.restaurant

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.extension.loadImage
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