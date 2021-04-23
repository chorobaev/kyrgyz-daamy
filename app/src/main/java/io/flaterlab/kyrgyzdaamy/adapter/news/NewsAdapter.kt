package io.flaterlab.kyrgyzdaamy.adapter.news

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.databinding.ItemDiscountBinding
import io.flaterlab.kyrgyzdaamy.extension.loadImageGlide
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse

class NewsAdapter(
    items: ArrayList<NewsResponse> = ArrayList()
) : GenericRecyclerAdapter<NewsResponse>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(ItemDiscountBinding::inflate)

    override fun bind(item: NewsResponse, holder: ViewHolder<*>) =
        with(holder.binding as ItemDiscountBinding) {
            discountTitle.text = item.name
            discountDescription.text = item.description
            discountImage.loadImageGlide(item.img_url)

        }
}