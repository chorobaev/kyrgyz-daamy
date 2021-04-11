package io.flaterlab.kyrgyzdaamy.adapter.food

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.kyrgyzdaamy.adapter.basket.CustomBasketModifiers
import io.flaterlab.kyrgyzdaamy.base.GenericRecyclerAdapter
import io.flaterlab.kyrgyzdaamy.base.ViewHolder
import io.flaterlab.kyrgyzdaamy.base.viewHolderFrom
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.Mode
import io.flaterlab.kyrgyzdaamy.databinding.ItemModificationBinding
import io.flaterlab.kyrgyzdaamy.extension.getIndex
import io.flaterlab.kyrgyzdaamy.service.response.BaseModifier
import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup

class CustomAdapterForMod(
    val list: ArrayList<BaseModifierGroup> = ArrayList(),
    private val mode: Mode,
    private val listener: ItemModGroupListener?
) : GenericRecyclerAdapter<BaseModifierGroup>(list), AddToBasketListener {
    private val modifiersMap = hashMapOf<Int, BaseModifier>()
    private val listOfModGroup = mutableListOf<BaseModifierGroup>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        parent.viewHolderFrom(
            ItemModificationBinding::inflate
        )

    override fun addToBasket(item: BaseModifier, baseModifierGroup: BaseModifierGroup) {
        modifiersMap[item.code] = item

        val modGroup = BaseModifierGroup(
            baseModifierGroup.schemeId,
            baseModifierGroup.groupId,
            baseModifierGroup.name,
            baseModifierGroup.maximumSelected,
            baseModifierGroup.minimumSelected,
            baseModifierGroup.freeCount,
            baseModifierGroup.changesPrice,
            baseModifierGroup.modifiers,
            modifiersMap
        )

        val indexGroup = listOfModGroup.getIndex(modGroup)
        if (indexGroup == -1) {
            listOfModGroup.add(modGroup)
        } else {
            listOfModGroup[indexGroup] = modGroup
        }

        listener?.addModGroup(listOfModGroup)
    }

    override fun bind(item: BaseModifierGroup, holder: ViewHolder<*>) =
        with(holder.binding as ItemModificationBinding) {
            groupName.text = item.name
            when (mode) {

                Mode.Basket -> {
                    val list = mutableListOf<BaseModifier>()
                    item.modifiersList.values.forEach { baseModifier ->
                        if (baseModifier.count > 0) {
                            list.add(baseModifier)
                        }
                    }
                    listViewModifier.adapter =
                        CustomBasketModifiers(list as ArrayList<BaseModifier>)
                }
                Mode.Editable -> {
                    var sum = 0
                    item.modifiersList.forEach {
                        sum += it.value.count
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
                Mode.NotBasket -> {
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

}