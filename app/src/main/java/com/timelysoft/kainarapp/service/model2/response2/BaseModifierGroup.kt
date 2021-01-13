package com.timelysoft.kainarapp.service.model2.response2

import com.google.gson.annotations.SerializedName

data class BaseModifierGroup(
    @SerializedName("schemeId") val schemeId : Int,
    @SerializedName("groupId") val name : String,
    @SerializedName("maximumSelected") val maximumSelected :Int,
    @SerializedName("minimumSelected") val minimumSelected : Int,
    @SerializedName("freeCount") val freeCount : Int,
    @SerializedName("changesPrice") val changesPrice : Boolean,
    @SerializedName("modifiers") var modifiers: MutableList<BaseModifier>,
    var modifiersList : HashMap<Int, BaseModifier>
)