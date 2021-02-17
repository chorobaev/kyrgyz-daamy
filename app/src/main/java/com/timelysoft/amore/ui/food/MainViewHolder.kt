package com.timelysoft.amore.ui.food


import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import com.timelysoft.amore.R
import com.timelysoft.amore.service.response.Category

class CountryViewHolder(itemView: View) : ChildViewHolder(itemView) {
    private val countryName: TextView = itemView.findViewById(R.id.child_item_name)

    fun bind(category: Category) {
        countryName.text = category.name
    }
}

class ContinentViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val continentName: TextView = itemView.findViewById(R.id.nameOfFood)
    fun bind(itemGroup: ItemsGroup) {
        val categoryModel = Gson().fromJson(itemGroup.category, Category::class.java)
        continentName.text = categoryModel.name
        /*
        Glide.with(imageView).load(AppPreferences.baseUrl + "api/restaurants/${AppPreferences.restaurant}/menu/items/${categoryModel.hashCode}/image")
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)

         */
    }

}