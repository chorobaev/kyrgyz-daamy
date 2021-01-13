package com.timelysoft.kainarapp.adapter.food

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.service.model2.response2.BaseModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import kotlinx.android.synthetic.main.item_modifier.view.*

class CustomAdapterForModifier(
    val list: ArrayList<BaseModifier>,
    val baseModifierGroup: BaseModifierGroup,
    private val listenerAddToBasket  : AddToBasketListener) :
    GenericRecyclerAdapter<BaseModifier>(list) {
    private var count = 0
    private var lastCheckedPosition = -1
    private var checkedItemCount: Int = 0
    private var sumOfCount: Int = 0

    init {
        checkedItemCount = list.count { it.isChecked }
    }

    override fun bind(item: BaseModifier, holder: ViewHolder) = with(holder.itemView) {
        checkbox.text = item.name



        checkbox.setOnClickListener {
            lastCheckedPosition = holder.adapterPosition
            notifyDataSetChanged()


            if (checkbox.isChecked) {
                if (sumOfCount >= baseModifierGroup.maximumSelected) {
                    Toast.makeText(
                        context,
                        "You can't select more than ${baseModifierGroup.maximumSelected} modifiers",
                        Toast.LENGTH_LONG
                    ).show()
                    checkbox.isChecked = false
                } else {
                    item.count = 1
                    listenerAddToBasket.addToBasket(BaseModifier(item.ident, item.name, item.code, item.price, item.limit, checkbox.isChecked, item.count), baseModifierGroup)
                    sumOfCount += 1
                    Toast.makeText(context, "$sumOfCount", Toast.LENGTH_LONG).show()
                    Toast.makeText(context, "itemCount: ${item.count}", Toast.LENGTH_LONG).show()
                }
            } else {
                sumOfCount -= item.count
                item_modifier_amount.text = "1"
                item.count = 0
                listenerAddToBasket.addToBasket(BaseModifier(item.ident, item.name, item.code, item.price, item.limit, checkbox.isChecked, item.count), baseModifierGroup)

            }

        }

        modPrice.text = if (item.price == 0) "" else item.price.toString()

        foodAmountMinus.setOnClickListener {

            if (checkbox.isChecked) {
                if (item.count>1) {
                    count = item_modifier_amount.text.toString().toInt()
                    count -= 1
                    sumOfCount -= 1
                    item.count = count
                    item_modifier_amount.text = count.toString()
                }

            }
            listenerAddToBasket.addToBasket(BaseModifier(item.ident, item.name, item.code, item.price, item.limit, checkbox.isChecked, count),baseModifierGroup)

        }
        foodAmountPlus.setOnClickListener {

            if (checkbox.isChecked) {
                if (sumOfCount >= baseModifierGroup.maximumSelected) {
                    Toast.makeText(context, "You can't take more than ${baseModifierGroup.maximumSelected} modifiers", Toast.LENGTH_LONG).show()
                }
                else{
                    if (item.count >= item.limit) {
                        Toast.makeText(context, "You can't take more than ${item.limit}", Toast.LENGTH_LONG).show()
                    } else {
                        count = item_modifier_amount.text.toString().toInt()
                        sumOfCount += 1
                        count += 1
                        Log.d("sumOF", sumOfCount.toString())
                        item.count = count
                        item_modifier_amount.text = count.toString()
                    }
                }
            }
            listenerAddToBasket.addToBasket(BaseModifier(item.ident, item.name, item.code, item.price, item.limit, checkbox.isChecked, item.count), baseModifierGroup)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_modifier).apply {

            if (baseModifierGroup.maximumSelected > 1) {
                itemView.plusMinus.visibility = View.VISIBLE

            } else {
                itemView.plusMinus.visibility = View.INVISIBLE
            }


        }
    }


}