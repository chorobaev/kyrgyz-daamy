package io.flaterlab.kyrgyzdaamy.adapter.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.R

class ShimmeringAdapter(private val layout: Layout, private val count: Int) :
    RecyclerView.Adapter<ShimmeringAdapter.ShimmerViewHolder>() {

    inner class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val layout: ShimmerFrameLayout = itemView.findViewById(R.id.shimmerLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.layout, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {}

    override fun getItemCount() = count
}

enum class Layout(val layout: Int) {
    Menu(R.layout.item_recycler_view),
    MenuItem(R.layout.menu_shimmer_item)
}
