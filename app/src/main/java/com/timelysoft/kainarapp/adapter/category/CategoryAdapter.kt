package com.timelysoft.kainarapp.adapter.category


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.extension.loadImage
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    private val listener: CategoryListener,
    items: ArrayList<Category> = ArrayList()
) : GenericRecyclerAdapter<Category>(items) {
    var row = -1

    override fun bind(item: Category, holder: ViewHolder) {

        holder.itemView.categoryName.text = item.name
        val url  = AppPreferences.baseUrl+"api/restaurants/${AppPreferences.restaurant}/menu/items/${item.hashCode}/image"
        holder.itemView.categoryPhoto.loadImage(url)

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(item)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_category)
    }

}