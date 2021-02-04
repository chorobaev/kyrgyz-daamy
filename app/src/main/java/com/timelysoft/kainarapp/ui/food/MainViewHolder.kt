package com.timelysoft.kainarapp.ui.food


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.Category

class CountryViewHolder(itemView: View) : ChildViewHolder(itemView) {
    private val countryName: TextView = itemView.findViewById(R.id.child_item_name)

    fun bind(category: Category) {
        countryName.text = category.name
    }
}

class ContinentViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val continentName: TextView = itemView.findViewById(R.id.nameOfFood)
    private val imageView : ImageView = itemView.findViewById(R.id.imageOfHeader)
    fun bind(itemGroup: ItemsGroup) {
        val categoryModel = Gson().fromJson(itemGroup.category, Category::class.java)
        continentName.text = categoryModel.name
        Glide.with(imageView).load(AppPreferences.baseUrl + "api/restaurants/${AppPreferences.restaurant}/menu/items/${categoryModel.hashCode}/image")
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }

}