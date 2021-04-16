package io.flaterlab.kyrgyzdaamy.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.adapter.food.FoodListener
import io.flaterlab.kyrgyzdaamy.databinding.ItemFoodBinding
import io.flaterlab.kyrgyzdaamy.extension.loadImageGlide
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import java.util.*
import kotlin.collections.ArrayList

class SearchMenuItemAdapter(private val menuItems: ArrayList<MenuItem>, private val listener:FoodListener) :
    RecyclerView.Adapter<SearchMenuItemAdapter.SearchViewHolder>(), Filterable {

    var filteredItems = arrayListOf<MenuItem>()


    init {
        filteredItems = menuItems
    }

    inner class SearchViewHolder(val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.foodImage.loadImageGlide(filteredItems[position].imageLink)
        holder.binding.foodDescription.text = filteredItems[position].description
        holder.binding.foodPrice.text = filteredItems[position].price.toString()
        holder.binding.foodWeight.text = filteredItems[position].weight.toString()
        holder.binding.foodTitle.text = filteredItems[position].name
        holder.binding.root.setOnClickListener {
            listener.onFoodClick(filteredItems[position], holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                filteredItems = if (charSearch.isEmpty()){
                    arrayListOf()
                }else {
                    val resultList = arrayListOf<MenuItem>()
                    for (item in menuItems) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))){
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredItems
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredItems = p1?.values as ArrayList<MenuItem>
                notifyDataSetChanged()
            }

        }
    }
}