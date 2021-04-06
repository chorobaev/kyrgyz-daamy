package com.timelysoft.amore.adapter.basket


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.CustomAdapterForMod
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.databinding.ItemBasketBinding
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.BaseModifierGroup
import com.timelysoft.amore.service.response.MenuItem

class BasketAdapter(
    private val listener: BasketListener,
    items: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(items) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = parent.viewHolderFrom(ItemBasketBinding::inflate)

    override fun bind(item: MenuItem, holder: ViewHolder<*>) = with(holder.binding as ItemBasketBinding) {

        amountPrice.text = "${item.price} ${AppPreferences.currencyName}"
        basketName.text =  "${item.amount} x ${item.name}"
        if (item.modifierGroups.isNotEmpty()) {
            itemBasketRecycler.adapter =
                CustomAdapterForMod(item.modifierGroups as ArrayList<BaseModifierGroup>, Mode.Basket, null)
        }
        sumAmount.text = "${item.priceWithMod} ${AppPreferences.currencyName}"


        basketEdit.setOnClickListener {
            listener.onClickItem(item)
        }

        basketDelete.setOnClickListener {
            listener.onDeleteItem(item)
        }
    }


}