package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName

class BaseModifierGroup(
    @SerializedName("schemeId") var schemeId : Int,
    @SerializedName("groupId") var groupId : String,
    @SerializedName("name") var name : String,
    @SerializedName("maximumSelected") var maximumSelected :Int,
    @SerializedName("minimumSelected") var minimumSelected : Int,
    @SerializedName("freeCount") var freeCount : Int,
    @SerializedName("changesPrice") var changesPrice : Boolean,
    @SerializedName("modifiers") var modifiers: MutableList<BaseModifier>,
    var modifiersList : HashMap<Int, BaseModifier>
){
    override fun toString(): String {
        return "[$schemeId,$groupId,$modifiersList]"
    }
}