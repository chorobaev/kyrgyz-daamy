package com.timelysoft.amore.adapter.restaurant

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.extension.loadImageWithoutCorner
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.model2.RestaurantResponse
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantAdapter(
    private val listener: RestaurantListener,
    items: ArrayList<RestaurantResponse> = ArrayList(),
    private val titleVisible: Boolean = false
) : GenericRecyclerAdapter<RestaurantResponse>(items) {
    override fun bind(item: RestaurantResponse, holder: ViewHolder) {

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