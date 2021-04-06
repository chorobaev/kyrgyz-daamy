package com.timelysoft.amore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timelysoft.amore.extension.countCost
import com.timelysoft.amore.extension.countPriceOfMod
import com.timelysoft.amore.service.response.BaseModifier
import com.timelysoft.amore.service.response.BaseModifierGroup
import com.timelysoft.amore.service.response.MenuItem

object BasketCommands {

    private val listHashMap = linkedMapOf<String, MenuItem>()

    private val mutableLiveDataWithHashMap = MutableLiveData<HashMap<String, MenuItem>>()

    val liveDataHashMap: LiveData<HashMap<String, MenuItem>>
        get() = mutableLiveDataWithHashMap
    val sumOfBasket = MutableLiveData<Int>()

    fun updateMenuItem(menuItem: MenuItem, hashCode: String) {
        if (listHashMap.remove(hashCode) != null) {
            Log.d("ListHashMap", listHashMap.keys.toString())
            Log.d("MenuItemToString", menuItem.toString())

            menuItem.modifierGroups.forEach {
                val filteredMap = it.modifiersList.filter { list ->
                    list.value.count != 0
                } as HashMap<Int, BaseModifier>
                it.modifiersList = filteredMap
            }
            val code = menuItem.toString()
            if (listHashMap.containsKey(code)) {
                val item = listHashMap[code]
                menuItem.amount += item!!.amount
            }
            listHashMap[code] = menuItem
            mutableLiveDataWithHashMap.value = listHashMap
            sumOfBasket.value = calculatePricesNew()
        }
    }

    fun deleteFromBasket(hashCode: String) {
        Log.d("ListHashMap", listHashMap.keys.toString())
        if (listHashMap.containsKey(hashCode)) {
            listHashMap.remove(hashCode)
        }
        mutableLiveDataWithHashMap.value = listHashMap
        sumOfBasket.value = calculatePricesNew()
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
            0
        )
    }

    fun basketIsEmpty(): Boolean {
        if (listHashMap.size > 0)
            return false
        return true
    }

    fun addMenuItem(menuItem: MenuItem, group: List<BaseModifierGroup>) {

        val basketItem = createNewMenuItem(menuItem, group)
        val key = basketItem.toString()
        val existsItem = listHashMap[key]
        if (existsItem != null) {
            basketItem.amount += existsItem.amount
        }

        listHashMap[key] = basketItem
        mutableLiveDataWithHashMap.value = listHashMap

        sumOfBasket.value = calculatePricesNew()

    }

    fun deleteAllFromBasket() {
        listHashMap.clear()
        mutableLiveDataWithHashMap.value = listHashMap
        sumOfBasket.value = 0
    }

    private fun calculatePricesNew(): Int {

        var totalBasketPrice = 0
        var priceOfMod = 0

        listHashMap.forEach {

            it.value.modifierGroups.forEach { baseModifierGroup ->
                priceOfMod += countPriceOfMod(baseModifierGroup)
            }

            val itemPrice = countCost(it.value.amount, it.value.price, priceOfMod)
            it.value.priceWithMod = itemPrice
            totalBasketPrice += itemPrice

        }

        return totalBasketPrice

    }


}