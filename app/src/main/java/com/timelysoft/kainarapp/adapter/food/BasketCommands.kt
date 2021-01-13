package com.timelysoft.kainarapp.adapter.food

import androidx.lifecycle.MutableLiveData
import com.timelysoft.kainarapp.extension.countCost
import com.timelysoft.kainarapp.extension.countPriceOfMod
import com.timelysoft.kainarapp.extension.getIndex
import com.timelysoft.kainarapp.service.model2.response2.BaseModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.MenuItem


object BasketCommands {
    val liveDataOfMenuItems = MutableLiveData<List<MenuItem>>()
    var listOfMenuItems = mutableListOf<MenuItem>()
    val sumOfBasket = MutableLiveData<Int>()

    fun insertMenuItemSecondVersion(menuItem: MenuItem, ind: Int, group: List<BaseModifierGroup>) {
        val basketItem = createNewMenuItem(menuItem, group)
        val index = listOfMenuItems.getIndex(basketItem, ind)
        if (index == -1) {
            listOfMenuItems.add(basketItem)
        } else {
            if (basketItem.amount == 0) {
                basketItem.amount = 1
            }
            val existsItem = listOfMenuItems[index]
            if (existsItem.modifierGroups.isNotEmpty() && group.isNotEmpty()) {
                when{
                    areModGroupAreEqual(basketItem.modifierGroups, existsItem.modifierGroups)-> updateMenuItem(existsItem, basketItem,index)
                    else-> insertMenuItemSecondVersion(basketItem, index+1, group)
                }

            }
            else{
                when{
                    group.isEmpty() && existsItem.modifierGroups.isEmpty() ->{
                      updateMenuItem(existsItem, basketItem, index)
                    }
                    else->{
                        listOfMenuItems.add(basketItem)
                    }
                }
            }
        }

        liveDataOfMenuItems.value = listOfMenuItems
        sumOfBasket.value = calculatePrices()

    }

    fun deleteAll(){

        listOfMenuItems.clear()
        liveDataOfMenuItems.value = listOfMenuItems
        sumOfBasket.value = 0
    }
    private fun updateMenuItem(existsMenuItem : MenuItem, newItem: MenuItem, index: Int){
        existsMenuItem.amount += newItem.amount
        listOfMenuItems[index] = existsMenuItem
    }

    private fun calculatePrices():Int{
        var totalBasketPrice = 0
        listOfMenuItems.forEach { menuItem ->
            var priceOfMod = 0
            menuItem.modifierGroups.forEach {baseModifierGroup->
                priceOfMod += countPriceOfMod(baseModifierGroup)
            }
            val itemPrice = countCost(menuItem.amount, menuItem.price, priceOfMod)
            menuItem.priceWithMod = itemPrice
            totalBasketPrice+=itemPrice
        }
        return totalBasketPrice
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

    private fun createNewMenuItem(menuItem: MenuItem, group: List<BaseModifierGroup>) : MenuItem{
        return MenuItem(
            menuItem.code,
            menuItem.description,
            menuItem.isHit,
            group,
            menuItem.name,
            menuItem.price,
            menuItem.recipe,
            menuItem.rests,
            menuItem.weight,
            menuItem.amount,
            false,
            0,
            menuItem.positionInList
        )
    }
    fun basketIsEmpty() : Boolean{
        if (listOfMenuItems.size>0)
            return false
        return true
    }



}