package com.timelysoft.amore.adapter.food

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemFoodBinding
import com.timelysoft.amore.extension.loadImageCoil
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.food.FoodAddToBasket



class FoodAdapter(
    private val listener: FoodListener,
    private val addToBasketListener: FoodAddToBasket,
    menuItems: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(menuItems) {
    override fun bind(item: MenuItem, holder: ViewHolder<*>)  = with(holder.binding as ItemFoodBinding){
        foodImage.loadImageCoil(item.imageLink)
        foodTitle.text = item.name
        foodPrice.text = (item.price).toString() + " ${AppPreferences.currencyName}"
        foodDescription.visibility = View.GONE
        root.setOnClickListener {
            listener.onFoodClick(item, holder.adapterPosition)
        }
        addToBasket.setOnClickListener {
            addToBasketListener.addToBasket(item, holder.adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  = parent.viewHolderFrom(ItemFoodBinding::inflate)


}