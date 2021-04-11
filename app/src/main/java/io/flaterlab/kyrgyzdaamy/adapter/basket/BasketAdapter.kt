package io.flaterlab.kyrgyzdaamy.adapter.basket


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.adapter.food.CustomAdapterForMod
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.Mode
import io.flaterlab.kyrgyzdaamy.databinding.ItemBasketBinding
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

class BasketAdapter(
    private val listener: BasketListener,
    items: ArrayList<MenuItem> = ArrayList()
) :
    GenericRecyclerAdapter<MenuItem>(items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(
            ItemBasketBinding::inflate
        )

    override fun bind(item: MenuItem, holder: ViewHolder<*>) =
        with(holder.binding as ItemBasketBinding) {

            amountPrice.text = "${item.price} ${AppPreferences.currencyName}"
            basketName.text = "${item.amount} x ${item.name}"
            if (item.modifierGroups.isNotEmpty()) {
                itemBasketRecycler.adapter =
                    CustomAdapterForMod(
                        item.modifierGroups as ArrayList<BaseModifierGroup>,
                        Mode.Basket,
                        null
                    )
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