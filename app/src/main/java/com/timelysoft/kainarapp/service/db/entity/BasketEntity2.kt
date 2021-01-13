package com.timelysoft.kainarapp.service.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.timelysoft.kainarapp.service.model2.response2.Modifier
import kotlinx.android.parcel.Parcelize



@Entity
@Parcelize
class BasketEntity2(
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value = "code")
    var id: Int,//id это code
    @SerializedName("name")var name: String,
    @SerializedName("price")var price: Int,
    @SerializedName("count")var count: Int,
    val rests: Int,
    var recipe: String,
    var image: String,
    var modifiers : List<Modifier>

) : Parcelable

