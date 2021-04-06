package com.timelysoft.amore.adapter.category


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemCategoryBinding
import com.timelysoft.amore.service.response.Category

class CategoryAdapter(
    private val listener: CategoryListener,
    items: ArrayList<Category> = ArrayList()
) : GenericRecyclerAdapter<Category>(items) {
    override fun bind(item: Category, holder: ViewHolder<*>)  = with(holder.binding as ItemCategoryBinding){
        nameOfFood.text = item.name
        icExpand.visibility = View.GONE

        parentItemContainer.setOnClickListener {
            listener.onCategoryClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  = parent.viewHolderFrom(ItemCategoryBinding::inflate)


}