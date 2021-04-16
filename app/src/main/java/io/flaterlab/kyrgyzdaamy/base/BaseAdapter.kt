package io.flaterlab.kyrgyzdaamy.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericRecyclerAdapter<T>(private var items: ArrayList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun bind(item: T, holder: ViewHolder<*>)

    fun set(items: ArrayList<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun add(items: ArrayList<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()

    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.count()


    fun position(position: Int): T {
        return items[position]
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bind(items[position], holder as ViewHolder<*>)

    }


}

class ViewHolder<T:ViewBinding> private constructor(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    constructor(
        parent: ViewGroup,
        creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
    ) : this(creator(
        LayoutInflater.from(parent.context),
        parent,
        false
    ))

}
fun <T : ViewBinding> ViewGroup.viewHolderFrom(
    creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
): ViewHolder<T> = ViewHolder(this, creator)