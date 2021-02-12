package com.timelysoft.amore.ui.food


import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.timelysoft.amore.service.model2.response2.Category


data class ItemsGroup(val list:  List<Category>, val category: String):
    ExpandableGroup<Category>(category, list)
