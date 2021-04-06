package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName


data class MenuItem(
        @SerializedName("code")val code: Int,
        @SerializedName("description")val description: String?,
        @SerializedName("isHit")val isHit: Int,
        @SerializedName("modifierGroups") var modifierGroups: List<BaseModifierGroup>,
        @SerializedName("name")val name: String,
        @SerializedName("price")val price: Int,
        @SerializedName("recipe")val recipe: String,
        @SerializedName("rests")val rests: Int,
        @SerializedName("imageLink") val imageLink : String,
        @SerializedName("weight")val weight: Int,
        var amount: Int,
        var priceWithMod : Int
){
        override fun toString(): String {
                return "[$code,$modifierGroups,$name]"
        }
}
