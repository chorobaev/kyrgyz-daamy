package com.timelysoft.kainarapp.adapter.food

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.loadImage
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import com.timelysoft.kainarapp.ui.food.FoodAddToBasket
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter(
    private val listener: FoodListener,
    private val addToBasketListener: FoodAddToBasket,
    menuItems: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(menuItems) {
    override fun bind(item: MenuItem, holder: ViewHolder) {

        val urlMod =
            AppPreferences.baseUrl + "api/restaurants/${AppPreferences.restaurant}/menu/items/${item.code}/image"
        holder.itemView.food_image.loadImage(urlMod)
        holder.itemView.food_title.text = item.name
        holder.itemView.food_price.text = (item.price).toString() + " Сом"
        //todo description
        //holder.itemView.food_description.text = item.recipe
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