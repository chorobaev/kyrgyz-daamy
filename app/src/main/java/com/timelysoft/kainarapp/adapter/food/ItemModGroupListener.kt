package com.timelysoft.kainarapp.adapter.food

import com.timelysoft.kainarapp.service.model2.ItemModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.MenuItem

interface ItemModGroupListener {
    fun addModGroup(group : List<BaseModifierGroup>)
}