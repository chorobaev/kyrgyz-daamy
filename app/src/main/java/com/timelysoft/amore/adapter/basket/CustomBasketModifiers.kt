package com.timelysoft.amore.adapter.basket

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemModBasketBinding
import com.timelysoft.amore.service.response.BaseModifier

class CustomBasketModifiers(val list: ArrayList<BaseModifier>)  :GenericRecyclerAdapter<BaseModifier>(list) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = parent.viewHolderFrom(ItemModBasketBinding::inflate)

    override fun bind(item: BaseModifier, holder: ViewHolder<*>)  = with(holder.binding as ItemModBasketBinding){
        if (item.price == 0){
            modifierPrice.visibility = View.GONE
        }
        itemModBasketModifierName.text = item.name
        amount.text = "${item.count}"
        modifierPrice.text = "${item.price}"
    }


}