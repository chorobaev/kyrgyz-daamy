package com.timelysoft.kainarapp.service.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class BasketEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value = "code")
    var id: Int,//id это code
    var name: String,
    var price: Int,
    var quantity: Int,
    val rests: Int,
    var recipe: String,
    var image: String

) : Parcelable