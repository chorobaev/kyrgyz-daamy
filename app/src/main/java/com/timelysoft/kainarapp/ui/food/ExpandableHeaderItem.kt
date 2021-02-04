package com.timelysoft.kainarapp.ui.food

import android.graphics.Typeface
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.Category
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_content.view.*

class ExpandableHeaderItem(val category: Category, val listener : OnExpandableAdapterClick)  : Item(), ExpandableItem{
    private lateinit var expandableGroup: ExpandableGroup


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.nameOfFood.text = category.name
        viewHolder.itemView.setBackgroundResource(R.drawable.background_categories)

        Glide.with(viewHolder.root.imageOfHeader)
            .load(AppPreferences.baseUrl + "api/restaurants/${AppPreferences.restaurant}/menu/items/${category.hashCode}/image")
            .apply(RequestOptions.circleCropTransform())
            .into(viewHolder.root.imageOfHeader)
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