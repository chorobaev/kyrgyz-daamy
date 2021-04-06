package com.timelysoft.amore.adapter.order

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemOrderItemsBinding
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.ResponseProductOrderState

class OrderItemsAdapter(items: ArrayList<ResponseProductOrderState> = ArrayList()) :
    GenericRecyclerAdapter<ResponseProductOrderState>(items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = parent.viewHolderFrom(ItemOrderItemsBinding::inflate)

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

    override fun bind(item: ResponseProductOrderState, holder: ViewHolder<*>) = with(holder.binding as ItemOrderItemsBinding){
        orderDetailCount.text = "${item.count} x"
        orderDetailName.text = item.name
        orderDetailPrice.text =
            (item.price * item.count).toString() + " ${AppPreferences.currencyName}"
        orderDetailDiscount.text =
            discount(item.discounted)
        orderDetailTotal.text = "Итог " + item.total.toString() + " ${AppPreferences.currencyName}"
    }
}