package com.timelysoft.amore.adapter.news

import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.base.viewHolderFrom
import com.timelysoft.amore.databinding.ItemDiscountBinding
import com.timelysoft.amore.extension.loadImageCoil
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.NewsResponse

class NewsAdapter(
    private val listener: NewsListener,
    items: ArrayList<NewsResponse> = ArrayList()
) : GenericRecyclerAdapter<NewsResponse>(items) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  = parent.viewHolderFrom(ItemDiscountBinding::inflate)
    override fun bind(item: NewsResponse, holder: ViewHolder<*>) = with(holder.binding as ItemDiscountBinding) {
        discountTitle.text = item.name
//        holder.itemView.discount_description.text = item.description

        discountImage.loadImageCoil(AppPreferences.baseUrl+"/"+ item.files.firstOrNull()?.relativeUrl)
        //val img = AppPreferences.baseUrl+"/"+ item.files.firstOrNull()?.relativeUrl
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            discountDescription.text = Html.fromHtml(item.boldDescription, Html.FROM_HTML_MODE_LEGACY).trim()
        } else {
            discountDescription.text = Html.fromHtml(item.boldDescription)
        }

        holder.itemView.setOnClickListener {
            listener.onDiscountCLick(item)
        }
    }
}