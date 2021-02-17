package com.timelysoft.amore.adapter.food

import androidx.lifecycle.MutableLiveData
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.extension.countCost
import com.timelysoft.amore.extension.countPriceOfMod
import com.timelysoft.amore.extension.getIndex
import com.timelysoft.amore.service.response.BaseModifier
import com.timelysoft.amore.service.response.BaseModifierGroup
import com.timelysoft.amore.service.response.MenuItem


object BasketCommands {
    val liveDataOfMenuItems = MutableLiveData<List<MenuItem>>()
    var listOfMenuItems = mutableListOf<MenuItem>()
    val sumOfBasket = MutableLiveData<Int>()


    fun insertMenuItemSecondVersion(
        menuItem: MenuItem,
        ind: Int,
        group: List<BaseModifierGroup>,
        mode: Mode
    ) {

        val basketItem = createNewMenuItem(menuItem, group)
        val index = listOfMenuItems.getIndex(basketItem, ind)
        if (index == -1) {
            listOfMenuItems.add(basketItem)
        } else {
            if (basketItem.amount == 0) {
                basketItem.amount = 1
            }
            val existsItem = listOfMenuItems[index]
            if (mode == Mode.Editable) {
                val map = updateEditableMenuItem(group, basketItem.positionInList)
                basketItem.modifierGroups = map
            } else {
                if (existsItem.modifierGroups.isNotEmpty() && group.isNotEmpty()) {
                    when {
                        areModGroupAreEqual(
                            basketItem.modifierGroups,
                            existsItem.modifierGroups
                        ) -> updateMenuItem(existsItem, basketItem, index, mode)
                        else -> insertMenuItemSecondVersion(basketItem, index + 1, group, mode)
                    }

                } else {
                    when {
                        group.isEmpty() && existsItem.modifierGroups.isEmpty() -> {
                            updateMenuItem(existsItem, basketItem, index, mode)
                        }
                        else -> {
                            listOfMenuItems.add(basketItem)
                        }
                    }
                }
            }
        }

        liveDataOfMenuItems.value = listOfMenuItems
        sumOfBasket.value = calculatePrices()
        refresh(mode)

    }

    fun refresh(mode: Mode){
        if (mode == Mode.Editable) {
            val data = listOfMenuItems.map { it }
            deleteAll()
            data.forEach {
                insertMenuItemSecondVersion(it, 0, it.modifierGroups, Mode.NotBasket)
            }
        }

    }

    fun deleteAll() {

        listOfMenuItems.clear()
        liveDataOfMenuItems.value = listOfMenuItems
        sumOfBasket.value = 0
    }

    private fun updateMenuItem(
        existsMenuItem: MenuItem,
        newItem: MenuItem,
        index: Int,
        mode: Mode
    ) {
        if (mode == Mode.Editable) {
            existsMenuItem.amount = newItem.amount
        } else {
            existsMenuItem.amount += newItem.amount
        }
        listOfMenuItems[index] = existsMenuItem
    }

    private fun calculatePrices(): Int {
        var totalBasketPrice = 0
        listOfMenuItems.forEach { menuItem ->
            var priceOfMod = 0
            menuItem.modifierGroups.forEach { baseModifierGroup ->
                priceOfMod += countPriceOfMod(baseModifierGroup)
            }
            val itemPrice = countCost(menuItem.amount, menuItem.price, priceOfMod)
            menuItem.priceWithMod = itemPrice
            totalBasketPrice += itemPrice
        }
        return totalBasketPrice
    }

    private fun updateEditableMenuItem(
        updatedModifierGroup: List<BaseModifierGroup>,
        position: Int
    ): List<BaseModifierGroup> {
        if (updatedModifierGroup.isNotEmpty()) {
            listOfMenuItems[position].modifierGroups.forEachIndexed { index, baseModifierGroup ->
                if (baseModifierGroup.maximumSelected == 1) {
                    val group = updatedModifierGroup[index].modifiersList.filter {
                        it.value.count > 0
                    }
                    baseModifierGroup.modifiersList = group as HashMap<Int, BaseModifier>

                } else {
                    val modifierList = hashMapOf<Int, BaseModifier>()
                    if (baseModifierGroup.modifiersList.isNotEmpty()) {

                        baseModifierGroup.modifiersList.forEach {
                            if (updatedModifierGroup[index].modifiersList.containsKey(it.value.code)) {
                                modifierList[it.value.code] =
                                    updatedModifierGroup[index].modifiersList[it.value.code]!!
                            } else {
                                modifierList[it.value.code] = it.value
                            }
                        }
                        baseModifierGroup.modifiersList = modifierList
                    }
                }
            }
            return listOfMenuItems[position].modifierGroups
        } else {
            return listOf()
        }
    }


    private fun areModGroupAreEqual(
        list: List<BaseModifierGroup>,
        list2: List<BaseModifierGroup>
    ): Boolean {

        var areEqual = false
        if (list.size != list2.size) {
            return false
        }
        list.forEachIndexed { index, _ ->
            if (areModifiersEqual(list[index].modifiersList, list2[index].modifiersList)) {
                areEqual = true
            } else {
                return false
            }
        }
        return areEqual
    }


    private fun areModifiersEqual(
        first: HashMap<Int, BaseModifier>,
        second: HashMap<Int, BaseModifier>
    ): Boolean {

        var areEqual = false
        if (first.size != second.size) {
            return false
        } else {
            val keys = first.keys
            keys.forEach {
                if (second.containsKey(it) && areAmountsAreEqual(
                        second[it]!!.count,
                        first[it]!!.count
                    )
                ) {
                    areEqual = true
                } else {
                    return false
                }
            }

        }
        return areEqual
    }

    private fun areAmountsAreEqual(amount: Int, amount2: Int): Boolean {
        return amount == amount2
    }

    fun deleteFromBasket(position: Int) {
        listOfMenuItems.removeAt(position)
        liveDataOfMenuItems.value = listOfMenuItems
        sumOfBasket.value = calculatePrices()
    }

    private fun createNewMenuItem(menuItem: MenuItem, group: List<BaseModifierGroup>): MenuItem {
        return MenuItem(
            menuItem.code,
            menuItem.description,
            menuItem.isHit,
            group,
            menuItem.name,
            menuItem.price,
            menuItem.recipe,
            menuItem.rests,
            menuItem.imageLink,
            menuItem.weight,
            menuItem.amount,
            false,
            0,
            menuItem.positionInList
        )
    }

    fun basketIsEmpty(): Boolean {
        if (listOfMenuItems.size > 0)
            return false
        return true
    }


}