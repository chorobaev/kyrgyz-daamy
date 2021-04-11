package io.flaterlab.kyrgyzdaamy.adapter.news

import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemDiscountBinding
import io.flaterlab.kyrgyzdaamy.extension.loadImageCoil
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse

class NewsAdapter(
    private val listener: NewsListener,
    items: ArrayList<NewsResponse> = ArrayList()
) : GenericRecyclerAdapter<NewsResponse>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemDiscountBinding::inflate)

    override fun bind(item: NewsResponse, holder: ViewHolder<*>) =
        with(holder.binding as ItemDiscountBinding) {
            discountTitle.text = item.name
//        holder.itemView.discount_description.text = item.description

            discountImage.loadImageCoil(AppPreferences.baseUrl + "/" + item.files.firstOrNull()?.relativeUrl)
            //val img = AppPreferences.baseUrl+"/"+ item.files.firstOrNull()?.relativeUrl
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                discountDescription.text =
                    Html.fromHtml(item.boldDescription, Html.FROM_HTML_MODE_LEGACY).trim()
            } else {
                discountDescription.text = Html.fromHtml(item.boldDescription)
            }

            holder.itemView.setOnClickListener {
                listener.onDiscountCLick(item)
            }
        }
}