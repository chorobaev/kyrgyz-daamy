package com.timelysoft.kainarapp.adapter.basket


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.food.CustomAdapterForMod
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.bottomsheet.basket.Mode

import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import kotlinx.android.synthetic.main.item_basket.view.*

class BasketAdapter(
    private val listener: BasketListener,
    items: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(items) {

    override fun bind(item: MenuItem, holder: ViewHolder) = with(holder.itemView) {
        item_modifier_amount.text = "${item.amount} x"
        amountPrice.text = "${item.price} сом"
        basket_name.text = item.name
        if (item.modifierGroups.isNotEmpty()) {
            item_basket_recycler.adapter =
                CustomAdapterForMod(item.modifierGroups as ArrayList<BaseModifierGroup>, Mode.Basket, null)
        }
        sumAmount.text = "${item.priceWithMod} сом"

        
        basket_edit.setOnClickListener {
            listener.onClickItem(item, holder.adapterPosition)
        }

        basket_delete.setOnClickListener {
            listener.onDeleteItem(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_basket)
    }
}