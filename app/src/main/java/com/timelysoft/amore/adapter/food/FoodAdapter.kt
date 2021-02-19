package com.timelysoft.amore.adapter.food

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.extension.loadImage
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.food.FoodAddToBasket
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter(
    private val listener: FoodListener,
    private val addToBasketListener: FoodAddToBasket,
    menuItems: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(menuItems) {
    override fun bind(item: MenuItem, holder: ViewHolder) {

        holder.itemView.food_image.loadImage(item.imageLink)
        holder.itemView.food_title.text = item.name
        holder.itemView.food_price.text = (item.price).toString() + " ${AppPreferences.currencyName}"
        holder.itemView.food_description.visibility = View.GONE
        holder.itemView.setOnClickListener {
            listener.onFoodClick(item, holder.adapterPosition)
        }
        holder.itemView.addToBasket.setOnClickListener {
            addToBasketListener.addToBasket(item, holder.adapterPosition)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_food)
    }
}