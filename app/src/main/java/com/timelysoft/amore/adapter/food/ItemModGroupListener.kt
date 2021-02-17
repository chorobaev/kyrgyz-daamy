package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.response.BaseModifierGroup

interface ItemModGroupListener {
    fun addModGroup(group : List<BaseModifierGroup>)
}