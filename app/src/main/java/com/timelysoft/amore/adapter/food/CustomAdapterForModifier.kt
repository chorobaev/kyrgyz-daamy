package com.timelysoft.amore.adapter.food

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.base.GenericRecyclerAdapter
import com.timelysoft.amore.base.ViewHolder
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.service.response.BaseModifier
import com.timelysoft.amore.service.response.BaseModifierGroup
import kotlinx.android.synthetic.main.item_modifier.view.*

class CustomAdapterForModifier(
    private val selectedModifiersList: HashMap<Int, BaseModifier>? = null,
    val list: ArrayList<BaseModifier>,
    private val baseModifierGroup: BaseModifierGroup,
    private val mode: Mode,
    private val listenerAddToBasket: AddToBasketListener,
    sum : Int = 0
) :
    GenericRecyclerAdapter<BaseModifier>(list) {
    private var count = 0
    private var sumOfCount: Int = sum



    override fun bind(item: BaseModifier, holder: ViewHolder) = with(holder.itemView) {

        if (selectedModifiersList != null) {
            println("SumOfCount: $sumOfCount")
            if (selectedModifiersList.containsKey(item.code)) {
                val modifier = selectedModifiersList[item.code]!!
                if (modifier.count >= 1) {
                    checkbox.isChecked = true
                }
                item.count = modifier.count
                item.isChecked = modifier.isChecked
                item_modifier_amount.text = item.count.toString()
                //item.isChecked = selectedModifiersList[item.code]!!.isChecked

            }

        }

        checkbox.text = item.name
        println("Checkbox: ${item.isChecked}")

        checkbox.setOnClickListener {

            if (checkbox.isChecked) {
                if (sumOfCount >= baseModifierGroup.maximumSelected) {
                    Toast.makeText(
                        context,
                        "Вы не можете выбрать больше чем ${baseModifierGroup.maximumSelected} модификаторов",
                        Toast.LENGTH_LONG
                    ).show()
                    checkbox.isChecked = false
                } else {

                    item.count = 1
                    item_modifier_amount.text = item.count.toString()

                    listenerAddToBasket.addToBasket(
                        BaseModifier(
                            item.ident,
                            item.name,
                            item.code,
                            item.price,
                            item.limit,
                            checkbox.isChecked,
                            item.count
                        ), baseModifierGroup
                    )
                    sumOfCount += 1
                }

            } else {

                sumOfCount -= item.count
                item_modifier_amount.text = "0"
                item.count = 0
                listenerAddToBasket.addToBasket(
                    BaseModifier(
                        item.ident,
                        item.name,
                        item.code,
                        item.price,
                        item.limit,
                        checkbox.isChecked,
                        item.count
                    ), baseModifierGroup
                )
            }

        }

        modPrice.text = if (item.price == 0) "" else item.price.toString()

        foodAmountMinus.setOnClickListener {

            if (checkbox.isChecked) {
                if (item.count > 1) {
                    count = item_modifier_amount.text.toString().toInt()
                    count -= 1
                    sumOfCount -= 1
                    item.count = count
                    item_modifier_amount.text = count.toString()
                }

            }
            listenerAddToBasket.addToBasket(
                BaseModifier(
                    item.ident,
                    item.name,
                    item.code,
                    item.price,
                    item.limit,
                    checkbox.isChecked,
                    count
                ), baseModifierGroup
            )

        }
        foodAmountPlus.setOnClickListener {

            if (checkbox.isChecked) {
                if (sumOfCount >= baseModifierGroup.maximumSelected) {
                    Toast.makeText(
                        context,
                        "Вы не можете выбрать больше чем ${baseModifierGroup.maximumSelected} модификаторов",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (item.count >= item.limit) {
                        Toast.makeText(
                            context,
                            "Вы не можете выбрать больше ${item.limit}",
                            Toast.LENGTH_LONG
                        ).show()
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
            listenerAddToBasket.addToBasket(
                BaseModifier(
                    item.ident,
                    item.name,
                    item.code,
                    item.price,
                    item.limit,
                    checkbox.isChecked,
                    item.count
                ), baseModifierGroup
            )
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