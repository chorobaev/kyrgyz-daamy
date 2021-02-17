package com.timelysoft.amore.ui.food

import android.graphics.Typeface
import com.timelysoft.amore.R
import com.timelysoft.amore.service.response.Category
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_category.view.*

class ExpandableHeaderItem(val category: Category, val listener : OnExpandableAdapterClick)  : Item(), ExpandableItem{
    private lateinit var expandableGroup: ExpandableGroup


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.nameOfFood.text = category.name
        viewHolder.itemView.setBackgroundResource(R.drawable.background_categories)

        viewHolder.itemView.setOnClickListener {
            expandableGroup.onToggleExpanded()
            listener.onItemClick(category)
            changeStuff(viewHolder)
        }
    }

    private fun changeStuff(viewHolder: GroupieViewHolder){
        viewHolder.root.icExpand.apply {
            if (expandableGroup.isExpanded){
                rotation = 360F
                viewHolder.root.nameOfFood.setTypeface(null, Typeface.BOLD)

            }
            else{
                viewHolder.root.nameOfFood.setTypeface(null, Typeface.NORMAL)
                rotation = 180F
            }

        }

    }

    override fun getLayout(): Int {
        return R.layout.item_category
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

}