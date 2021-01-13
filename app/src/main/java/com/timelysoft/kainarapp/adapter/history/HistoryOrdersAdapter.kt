package com.timelysoft.kainarapp.adapter.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.toMyDateTime
import com.timelysoft.kainarapp.extension.toMyTimezone
import com.timelysoft.kainarapp.service.model.OrderItemModel
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryOrdersAdapter(
    private val listener : HistoryOrderListener,
    items: ArrayList<OrderItemModel> = ArrayList()
) : GenericRecyclerAdapter<OrderItemModel>(items) {
    override fun bind(item: OrderItemModel, holder: ViewHolder) {
        holder.itemView.history_date.text = item.rkOrderDate.toMyDateTime().toMyTimezone()
        holder.itemView.history_cost.text = "№ ${item.rkOrderNum}"

        holder.itemView.setOnClickListener {
            listener.onClickHistoryOrder(item.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_history)
    }

}