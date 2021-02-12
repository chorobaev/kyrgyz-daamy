package com.timelysoft.amore.extension

import com.timelysoft.amore.service.model2.response2.BaseModifierGroup
import com.timelysoft.amore.service.model2.response2.MenuItem

fun List<MenuItem>.getIndex(menuItem: MenuItem, index : Int) : Int{
    if (this.isEmpty()) {
        return -1
    } else {

        for (i in index until this.size){
            if ((this[i].code == menuItem.code && this[i].modifierGroups.isNotEmpty()) || (this[i].code == menuItem.code && this[i].modifierGroups.isEmpty())){
                return i
            }
        }
        return -1
    }
}


fun List<BaseModifierGroup>.getIndex(modifier: BaseModifierGroup): Int {
    if (this.isEmpty()) {
        return -1
    } else {
        this.forEachIndexed { index, baseModifierGroup ->
            if (baseModifierGroup.schemeId == modifier.schemeId) {
                return index
            }
        }
        return -1
    }
}

