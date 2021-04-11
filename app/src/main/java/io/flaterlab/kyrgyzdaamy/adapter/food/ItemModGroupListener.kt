package io.flaterlab.kyrgyzdaamy.adapter.food

import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup

interface ItemModGroupListener {
    fun addModGroup(group : List<BaseModifierGroup>)
}