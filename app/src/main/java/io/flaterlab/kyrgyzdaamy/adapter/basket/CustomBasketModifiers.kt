package io.flaterlab.kyrgyzdaamy.adapter.basket

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemModBasketBinding
import io.flaterlab.kyrgyzdaamy.service.response.BaseModifier

class CustomBasketModifiers(val list: ArrayList<BaseModifier>) :
    GenericRecyclerAdapter<BaseModifier>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemModBasketBinding::inflate)

    override fun bind(item: BaseModifier, holder: ViewHolder<*>) =
        with(holder.binding as ItemModBasketBinding) {
            if (item.price == 0) {
                modifierPrice.visibility = View.GONE
            }
            itemModBasketModifierName.text = item.name
            amount.text = "${item.count}"
            modifierPrice.text = "${item.price}"
        }


}