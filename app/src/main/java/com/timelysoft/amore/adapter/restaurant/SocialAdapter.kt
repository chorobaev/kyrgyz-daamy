package com.timelysoft.amore.adapter.restaurant

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.service.model.SocialNetWorkEnum
import com.timelysoft.amore.service.model2.SocialNetwork
import kotlinx.android.synthetic.main.item_social_networks.view.*

class SocialAdapter(
    private val listener: SocialListener,
    items: ArrayList<SocialNetwork> = ArrayList()
) : GenericRecyclerAdapter<SocialNetwork>(items) {
    override fun bind(item: SocialNetwork, holder: ViewHolder) {
        when (item.type) {
            SocialNetWorkEnum.instagram -> holder.itemView.social_logo.setImageResource(R.drawable.ic_instagram)
            SocialNetWorkEnum.facebook -> holder.itemView.social_logo.setImageResource(R.drawable.ic_fb)
            SocialNetWorkEnum.twitter -> holder.itemView.social_logo.setImageResource(R.drawable.ic_twitter)
            SocialNetWorkEnum.vk -> holder.itemView.social_logo.setImageResource(R.drawable.ic_vk)
            SocialNetWorkEnum.youtube -> holder.itemView.social_logo.setImageResource(R.drawable.ic_youtube)
            SocialNetWorkEnum.whatsapp -> holder.itemView.social_logo.setImageResource(R.drawable.ic_whatsapp)
            SocialNetWorkEnum.ok -> holder.itemView.social_logo.setImageResource(R.drawable.ic_ok)
        }

        holder.itemView.setOnClickListener {
            listener.onSocialClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_social_networks)
    }

}