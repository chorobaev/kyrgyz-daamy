package com.timelysoft.kainarapp.adapter.restaurant

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.loadImageWithoutCorner
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.Restaurant
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantAdapter(
    private val listener: RestaurantListener,
    items: ArrayList<RestaurantResponse> = ArrayList(),
    private val titleVisible: Boolean = false
) : GenericRecyclerAdapter<RestaurantResponse>(items) {
    override fun bind(item: RestaurantResponse, holder: ViewHolder) {
        //holder.itemView.restaurant_image.loadImageWithoutCorner(AppPreferences.baseUrl + item.restaurantDetail?.relativePictureUrl)

        holder.itemView.restaurant_image.loadImageWithoutCorner(AppPreferences.baseUrl +item.logo.relativeUrl)
        holder.itemView.restaurant_title.text = item.name

        holder.itemView.setOnClickListener {
            AppPreferences.bankPay = item.onlinePaymentSupported
            println("CrmID: "+item.crmId)
            listener.onRestaurantClick(
                item.id,
                "",
                AppPreferences.baseUrl + item.logo.relativeUrl,
                item.crmId
            )

        }
        if (titleVisible) {
            holder.itemView.restaurant_title.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_restaurant)
    }

}