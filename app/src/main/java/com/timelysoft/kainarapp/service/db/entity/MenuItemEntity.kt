package com.timelysoft.kainarapp.service.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup

@Entity
class MenuItemEntity(
    @PrimaryKey
    var id: Int?,
    var code: Int,
    var name: String,
    var description : String,
    var price: Int,
    var image: String,
    val rests: Int,
    var id_category: Int,
    var recipe:String

)

