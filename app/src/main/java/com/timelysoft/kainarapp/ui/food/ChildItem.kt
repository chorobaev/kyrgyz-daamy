package com.timelysoft.kainarapp.ui.food

import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.service.model2.response2.Category
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_content.view.*

open class ChildItem( val category: Category, val listener : OnChildItemListener) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.setBackgroundResource(R.drawable.background_categories)
        viewHolder.itemView.setOnClickListener {
            listener.onChildClick(category)
        }
        viewHolder.root.child_item_name.text = category.name

    }
    override fun getLayout(): Int {
        return R.layout.item_content
    }

}