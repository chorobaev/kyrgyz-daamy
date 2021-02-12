package com.timelysoft.amore.adapter.news

import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.extension.loadImage
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.model2.response2.NewsResponse
import kotlinx.android.synthetic.main.item_discount.view.*

class NewsAdapter(
    private val listener: NewsListener,
    items: ArrayList<NewsResponse> = ArrayList()
) : GenericRecyclerAdapter<NewsResponse>(items) {
    override fun bind(item: NewsResponse, holder: ViewHolder) {

        holder.itemView.discount_title.text = item.name
//        holder.itemView.discount_description.text = item.description

        holder.itemView.discount_image.loadImage(AppPreferences.baseUrl+"/"+ item.files.firstOrNull()?.relativeUrl)
        //val img = AppPreferences.baseUrl+"/"+ item.files.firstOrNull()?.relativeUrl
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.itemView.discount_description.text = Html.fromHtml(item.boldDescription, Html.FROM_HTML_MODE_LEGACY).trim()
        } else {
            holder.itemView.discount_description.text = Html.fromHtml(item.boldDescription)
        }

        holder.itemView.setOnClickListener {
            listener.onDiscountCLick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_discount)
    }
}