package io.flaterlab.kyrgyzdaamy.adapter.category


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemCategoryBinding
import io.flaterlab.kyrgyzdaamy.service.response.Category

class CategoryAdapter(
    private val listener: CategoryListener,
    items: ArrayList<Category> = ArrayList()
) : GenericRecyclerAdapter<Category>(items) {
    override fun bind(item: Category, holder: ViewHolder<*>) =
        with(holder.binding as ItemCategoryBinding) {
            nameOfFood.text = item.name
            icExpand.visibility = View.GONE

            parentItemContainer.setOnClickListener {
                listener.onCategoryClick(item)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemCategoryBinding::inflate)


}