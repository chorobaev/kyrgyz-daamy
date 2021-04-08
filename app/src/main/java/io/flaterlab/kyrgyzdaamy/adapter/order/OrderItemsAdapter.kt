package io.flaterlab.kyrgyzdaamy.adapter.order

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemOrderItemsBinding
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.ResponseProductOrderState

class OrderItemsAdapter(items: ArrayList<ResponseProductOrderState> = ArrayList()) :
    GenericRecyclerAdapter<ResponseProductOrderState>(items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemOrderItemsBinding::inflate)

    private fun discount(discount: Int): String {
        return when {
            discount == 0 -> "Скидка 0 ${AppPreferences.currencyName}"
            discount > 0 -> {
                "Скидка ${discount * -1} ${AppPreferences.currencyName}"
            }
            discount < 0 -> {
                "Наценка +${discount * -1} ${AppPreferences.currencyName}"
            }
            else -> "Скидка 0 ${AppPreferences.currencyName}"
        }
    }

    override fun bind(item: ResponseProductOrderState, holder: ViewHolder<*>) =
        with(holder.binding as ItemOrderItemsBinding) {
            orderDetailCount.text = "${item.count} x"
            orderDetailName.text = item.name
            orderDetailPrice.text =
                (item.price * item.count).toString() + " ${AppPreferences.currencyName}"
            orderDetailDiscount.text =
                discount(item.discounted)
            orderDetailTotal.text =
                "Итог " + item.total.toString() + " ${AppPreferences.currencyName}"
        }
}