package com.timelysoft.amore.adapter.order


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemOrderDiscountBinding
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.Discount


class OrderDiscountAdapter(
    items: ArrayList<Discount> = ArrayList(),
    private val isHistory: Boolean = false
) :
    GenericRecyclerAdapter<Discount>(items) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = parent.viewHolderFrom(ItemOrderDiscountBinding::inflate)

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

    override fun bind(item: Discount, holder: ViewHolder<*>) = with(holder.binding as ItemOrderDiscountBinding) {
        orderDiscountName.text = item.name
        if (isHistory) {
            orderDiscountTotal.text =  "${item.total } ${AppPreferences.currencyName}"
        } else {
            orderDiscountTotal.text = discount(item.total.toDouble())
        }
    }

}