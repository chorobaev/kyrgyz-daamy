package com.timelysoft.kainarapp.adapter.category


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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

    override fun bind(item: Category, holder: ViewHolder) {

        holder.itemView.nameOfFood.text = item.name
        val url  = AppPreferences.baseUrl+"api/restaurants/${AppPreferences.restaurant}/menu/items/${item.hashCode}/image"
        Glide.with(holder.itemView.imageOfHeader)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.itemView.imageOfHeader)
        holder.itemView.icExpand.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(item)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_category)
    }

}