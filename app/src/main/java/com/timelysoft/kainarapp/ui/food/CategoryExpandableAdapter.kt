package com.timelysoft.kainarapp.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.service.model2.response2.Category

class CategoryExpandableAdapter(groups: List<ExpandableGroup<*>>?) :
    ExpandableRecyclerViewAdapter<ContinentViewHolder, CountryViewHolder>(
        groups
    ) {
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): ContinentViewHolder {
        val itemView =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_category, parent, false)
        return ContinentViewHolder(itemView)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CountryViewHolder {
        val itemView =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_content, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindChildViewHolder(
        holder: CountryViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        childIndex: Int
    ) {
        val country: Category = group?.items?.get(childIndex) as Category
        holder?.bind(country)
    }

    override fun onBindGroupViewHolder(
        holder: ContinentViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        val continent: ItemsGroup = group as ItemsGroup
        holder?.bind(continent)
    }
}