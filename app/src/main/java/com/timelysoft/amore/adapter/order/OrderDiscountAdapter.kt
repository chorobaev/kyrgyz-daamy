package com.timelysoft.amore.adapter.order


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.Discount
import kotlinx.android.synthetic.main.item_order_discount.view.*


class OrderDiscountAdapter(
    items: ArrayList<Discount> = ArrayList(),
    private val isHistory: Boolean = false
) :
    GenericRecyclerAdapter<Discount>(items) {
    override fun bind(item: Discount, holder: ViewHolder) {
        holder.itemView.order_discount_name.text = item.name
        if (isHistory) {
            holder.itemView.order_discount_total.text =  "${item.total } ${AppPreferences.currencyName}"
        } else {
            holder.itemView.order_discount_total.text = discount(item.total.toDouble())
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_order_discount)
    }

    private fun discount(discount: Double): String {
        return when {
            discount == 0.0 -> "Скидка 0 ${AppPreferences.currencyName}"
            discount > 0 -> {
                "${discount * -1} ${AppPreferences.currencyName}"
            }
            discount < 0 -> {
                "+${discount * -1} ${AppPreferences.currencyName}"
            }
            else -> "Скидка 0 ${AppPreferences.HEADER_CACHE_CONTROL}"
        }
    }

}