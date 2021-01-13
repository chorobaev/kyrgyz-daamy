package com.timelysoft.kainarapp.adapter.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.restaurant.RestaurantListener
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.loadImage
import com.timelysoft.kainarapp.service.model2.response2.CityRestResponse
import com.timelysoft.kainarapp.service.response.CityAndStreetResponse
import com.timelysoft.kainarapp.service.response.RestaurantInfoResponse
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_restaurant.view.*

/*
class HistoryRestaurantAdapter(
    private val listener: RestaurantListener,
    items: ArrayList<CityRestResponse> = ArrayList()
) : GenericRecyclerAdapter<CityRestResponse>(items) {
    override fun bind(item: CityRestResponse, holder: ViewHolder) {

       // holder.itemView.category_image.loadImage(item.restaurantDetail?.pictureUrl)
        holder.itemView.categoryName.text = item.name
        holder.itemView.setOnClickListener {
            listener.onRestaurantClick(item.id.toString(), "")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_category)
    }

}

 */