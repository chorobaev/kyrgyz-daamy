package io.flaterlab.kyrgyzdaamy.adapter.food

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemFoodBinding
import io.flaterlab.kyrgyzdaamy.extension.loadImageCoil
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.food.FoodAddToBasket


class FoodAdapter(
    private val listener: FoodListener,
    private val addToBasketListener: FoodAddToBasket,
    menuItems: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(menuItems) {
    override fun bind(item: MenuItem, holder: ViewHolder<*>) =
        with(holder.binding as ItemFoodBinding) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemFoodBinding::inflate)


}