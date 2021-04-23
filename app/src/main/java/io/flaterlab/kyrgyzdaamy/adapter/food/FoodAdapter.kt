package io.flaterlab.kyrgyzdaamy.adapter.food

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemFoodBinding
import io.flaterlab.kyrgyzdaamy.extension.loadImageGlide
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.food.FoodAddToBasket
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList


class FoodAdapter(
    private val listener: FoodListener,
    private val addToBasketListener: FoodAddToBasket,
    private val menuItems: ArrayList<MenuItem>
) :
    GenericRecyclerAdapter<MenuItem>(menuItems) {


    override fun bind(item: MenuItem, holder: ViewHolder<*>) =
        with(holder.binding as ItemFoodBinding) {

            foodImage.loadImageGlide(item.imageLink)
            //map[item.imageLink]?.let { foodImage.loadImageCoil(it) }

            foodTitle.text = item.name
            foodWeight.text = "${item.weight} г"
            foodPrice.text = (item.price).toString() + " ${AppPreferences.currencyName}"
            foodDescription.visibility = View.GONE
            root.setOnClickListener {
                listener.onFoodClick(item, holder.adapterPosition)
            }

        }

    override fun getItemCount(): Int = menuItems.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemFoodBinding::inflate)


}