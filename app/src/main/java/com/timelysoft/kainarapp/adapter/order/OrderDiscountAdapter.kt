package com.timelysoft.kainarapp.adapter.order


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.service.model2.response2.Discount
import kotlinx.android.synthetic.main.item_order_discount.view.*


class OrderDiscountAdapter(
    items: ArrayList<Discount> = ArrayList(),
    private val isHistory: Boolean = false
) :
    GenericRecyclerAdapter<Discount>(items) {
    override fun bind(item: Discount, holder: ViewHolder) {
        holder.itemView.order_discount_name.text = item.name
        if (isHistory) {
            holder.itemView.order_discount_total.text =  "${item.total } сом"
        } else {
            holder.itemView.order_discount_total.text = discount(item.total.toDouble())
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_order_discount)
    }

    private fun discount(discount: Double): String {
        return when {
            discount == 0.0 -> "Скидка 0 сом"
            discount > 0 -> {
                "${discount * -1} сом"
            }
            discount < 0 -> {
                "+${discount * -1} сом"
            }
            else -> "Скидка 0 сом"
        }
    }

}