package io.flaterlab.kyrgyzdaamy.service.response

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName


data class MenuItem(
    @SerializedName("description") val description: String?,
    @SerializedName("modifierGroups") var modifierGroups: List<BaseModifierGroup>,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("recipe") val recipe: String,
    @SerializedName("rests") val rests: Int,
    @SerializedName("imageLink") var imageLink: String,
    @SerializedName("weight") val weight: Int,
    var amount: Int,
    var priceWithMod: Int
) {
    constructor():this("",listOf(), "",0,"",1,"",0,0,0)
    override fun toString(): String {
        return "[$modifierGroups,$name]"
    }

    companion object{
        fun DocumentSnapshot.toMenuItem():MenuItem?{
            return try {
                this.toObject(MenuItem::class.java)
            }catch (e:Exception){
                null
            }
        }
    }
}



