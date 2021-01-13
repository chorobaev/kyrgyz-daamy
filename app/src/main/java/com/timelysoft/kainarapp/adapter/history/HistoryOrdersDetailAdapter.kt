package com.timelysoft.kainarapp.adapter.history

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.service.model.HistoryMenuItems
import kotlinx.android.synthetic.main.item_basket.view.*
import kotlinx.android.synthetic.main.item_history_detail.view.*

class HistoryOrdersDetailAdapter(
    items: ArrayList<HistoryMenuItems> = ArrayList()
) : GenericRecyclerAdapter<HistoryMenuItems>(items) {
    override fun bind(item: HistoryMenuItems, holder: ViewHolder) {
        val view = holder.itemView
        view.basket_delete.visibility = View.GONE
        view.item_basket_sum.visibility = View.GONE
        view.item_modifier_amount.text = "${item.quantity} x "
        view.basket_name.text = item.name
        view.amountPrice.text = "${item.sum} сом"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_basket)
    }

}