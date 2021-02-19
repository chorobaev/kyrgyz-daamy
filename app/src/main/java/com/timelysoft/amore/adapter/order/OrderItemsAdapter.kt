package com.timelysoft.amore.adapter.order

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.ResponseProductOrderState
import kotlinx.android.synthetic.main.item_order_items.view.*

class OrderItemsAdapter(items: ArrayList<ResponseProductOrderState> = ArrayList()) :
    GenericRecyclerAdapter<ResponseProductOrderState>(items) {
    override fun bind(item: ResponseProductOrderState, holder: ViewHolder) {
        holder.itemView.order_detail_count.text = "${item.count} x"
        holder.itemView.order_detail_name.text = item.name
        holder.itemView.order_detail_price.text =
            (item.price * item.count).toString() + " ${AppPreferences.currencyName}"
        holder.itemView.order_detail_discount.text =
            discount(item.discounted)
        holder.itemView.order_detail_total.text = "Итог " + item.total.toString() + " ${AppPreferences.currencyName}"

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_order_items)
    }

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
}