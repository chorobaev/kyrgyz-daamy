package com.timelysoft.kainarapp.adapter.food

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.basket.CustomBasketModifiers
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarapp.bottomsheet.basket.Mode
import com.timelysoft.kainarapp.extension.getIndex
import com.timelysoft.kainarapp.service.model2.response2.BaseModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import kotlinx.android.synthetic.main.item_modification.view.*

class CustomAdapterForMod(
    val list: ArrayList<BaseModifierGroup> = ArrayList(),
    private val mode : Mode,
    val listener: ItemModGroupListener?
) : GenericRecyclerAdapter<BaseModifierGroup>(list), AddToBasketListener {
    private val modifiersMap = hashMapOf<Int, BaseModifier>()
    private val listOfModGroup = mutableListOf<BaseModifierGroup>()
    override fun bind(item: BaseModifierGroup, holder: ViewHolder) = with(holder.itemView) {
        groupName.text = item.name
        when(mode){

            Mode.Basket->{
                val list = mutableListOf<BaseModifier>()
                item.modifiersList.values.forEach { baseModifier ->
                    if (baseModifier.count>0) {
                        list.add(baseModifier)
                    }
                }
                listViewModifier.adapter =
                    CustomBasketModifiers(list as ArrayList<BaseModifier>)
            }
            Mode.Editable->{
                var sum = 0
                item.modifiersList.forEach {
                    sum+=it.value.count
                }
                listViewModifier.adapter = CustomAdapterForModifier(
                    item.modifiersList,
                    item.modifiers as ArrayList<BaseModifier>,
                    item,
                    Mode.Editable,
                    this@CustomAdapterForMod,
                    sum

                )
            }
            Mode.NotBasket->{
                listViewModifier.adapter = CustomAdapterForModifier(
                    null,
                    item.modifiers as ArrayList<BaseModifier>,
                    item,
                    Mode.NotBasket,
                    this@CustomAdapterForMod
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_modification)
    }

    override fun addToBasket(item: BaseModifier, baseModifierGroup: BaseModifierGroup) {
        modifiersMap[item.code] = item

        val modGroup = BaseModifierGroup(baseModifierGroup.schemeId, baseModifierGroup.groupId,baseModifierGroup.name,
        baseModifierGroup.maximumSelected, baseModifierGroup.minimumSelected, baseModifierGroup.freeCount, baseModifierGroup.changesPrice,
            baseModifierGroup.modifiers, modifiersMap)

        val indexGroup = listOfModGroup.getIndex(modGroup)
        if (indexGroup == -1){
            listOfModGroup.add(modGroup)
        }
        else{
            listOfModGroup[indexGroup] = modGroup
        }

        listener?.addModGroup(listOfModGroup)
    }

}