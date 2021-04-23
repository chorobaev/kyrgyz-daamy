package io.flaterlab.kyrgyzdaamy.extension

import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

fun List<MenuItem>.getIndex(menuItem: MenuItem, index: Int): Int {
    if (this.isEmpty()) {
        return -1
    } else {

        for (i in index until this.size) {
            if (this[i].name == menuItem.name) {
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

