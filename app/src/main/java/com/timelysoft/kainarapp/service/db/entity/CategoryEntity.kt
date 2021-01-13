package com.timelysoft.kainarapp.service.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CategoryEntity(
        @PrimaryKey
        var id: Int,
        var name: String,
        var imageUrl: String
)