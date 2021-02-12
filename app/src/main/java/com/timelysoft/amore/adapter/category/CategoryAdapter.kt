package com.timelysoft.amore.adapter.category


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.model2.response2.Category
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
        holder.itemView.setBackgroundResource(R.drawable.background_categories)

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(item)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_category)
    }

}